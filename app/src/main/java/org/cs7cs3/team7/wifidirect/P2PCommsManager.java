package org.cs7cs3.team7.wifidirect;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.Models.MatchingResult;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/***
 * Every device runs a server socket to listen for messagesReceived
 * Client devices should only listen for messagesReceived indicating matching results
 * Group owner device will listen to only messagesReceived from clients indicating their journey information
 */
public class P2PCommsManager implements ICommsManager {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI";

    private Context context;
    private String userLoginOnLocal;
    private P2PNetworkManager networkManager;
    private Thread networkManagerMonitorThread;
    private String serverIPAddress;
    private boolean thisDeviceActsAsServer;
    private boolean thisDeviceConnected;

    private Thread receiverThread;

    private Queue<Message> pendingMessagesToSendQueue;

    //Only used when this device is acting as server
    private Map<String, String> clients;

    private DistributedMatching p2pMatching;

    public P2PCommsManager(Context context, String userLoginOnLocal) {
        this.context = context;
        this.userLoginOnLocal = userLoginOnLocal;
        this.networkManager = new P2PNetworkManager(context, this);
        this.thisDeviceActsAsServer = false;
        this.thisDeviceConnected = false;
        this.p2pMatching = new DistributedMatching(this);
        this.pendingMessagesToSendQueue = new ConcurrentLinkedQueue<Message>();
        //maps user ids to IPs
        this.clients = new HashMap<String, String>();
    }

    /***
     * Wrapper method that the UI can use to request a Journey Match
     */
    public void requestJourneyMatch(JourneyRequest journeyRequest) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN P2PCommsManager.requestJourneyMatch()");
        Log.d(WIFI_P2P_DEBUG_LABEL, "journeyRequest: " + journeyRequest);
        //At this point, if there is no connection, the message will be saved with destinationIP = null
        //Thus, when pulling the messages, it is necessary to check if destinationIP is null and if so, replacing with
        //serverIPAddress
        Message message = new Message(this.serverIPAddress, MESSAGE_TYPES.JOURNEY_MATCH_REQUEST);
        message.setPayload(journeyRequest);
        Log.d(WIFI_P2P_DEBUG_LABEL, "Adding a message to the queue");
        pendingMessagesToSendQueue.add(message);

        if (this.thisDeviceConnected) {
            sendMessage();
        } else {
            Log.d(WIFI_P2P_DEBUG_LABEL, "You are not in the network. reset network initiated");
            if (networkManagerMonitorThread == null || networkManagerMonitorThread.isAlive() == false) {
                //This thread is to monitor network conditions in timely manner and take proper actions (e.g., reset network, log group owner)
                networkManagerMonitorThread = new Thread(new NetworkManagerMonitor((P2PNetworkManager) networkManager));
                Log.d(WIFI_P2P_DEBUG_LABEL, "Starting NetworkManagerMonitorThread");
                networkManagerMonitorThread.start();
            }
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "END P2PCommsManager.sendMessage()");
    }

    /***
     * Wrapper method that the matching process can use to inform peers of result of the matching
     */
    public void broadcastMatchingResult(List<MatchingResult> matchingResultList) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN P2PCommsManager.broadcastMatchingResult()");
        Log.d(WIFI_P2P_DEBUG_LABEL, "Count of groups: " + matchingResultList.size());
        for (MatchingResult matchingResult : matchingResultList) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Count of one group: " + matchingResult.getGroupMembers().size());
            for (User user : matchingResult.getGroupMembers()) {
                String destinationIP = clients.get(user.getLogin());
                Log.d(WIFI_P2P_DEBUG_LABEL, "User in the group: " + user);
                if (destinationIP != null) {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "Preparing message for destinationIP: " + destinationIP);
                    Message message = new Message(destinationIP, MESSAGE_TYPES.MATCHING_RESULT);
                    message.setPayload(matchingResult);
                    Log.d(WIFI_P2P_DEBUG_LABEL, "Adding a message to the queue");
                    Log.d(WIFI_P2P_DEBUG_LABEL, "Message: " + message);
                    pendingMessagesToSendQueue.add(message);
                    if (this.thisDeviceConnected) {
                        sendMessage();
                    } else {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "You are not in the network. Aborting.");
                    }
                } else {
                    //If this is a message for this device
                    if (user.getLogin().equals(userLoginOnLocal)) {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Message for this device, so no need to actually send a message. Instead, raise a broadcast");
                        //Raise local broadcast to update interface with the match result
                        Intent intent = new Intent(Constants.JOURNEY_MATCH_RESULT_INTENT_ACTION);
                        intent.putExtra(Constants.JOURNEY_MATCH_RESULT_INTENT_ACTION_PARCELABLE_KEY, matchingResult);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                    else {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Skipping message with null destinationIP since there isn't supposed to be one of that kind on this method");
                    }
                }
            }
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "END P2PCommsManager.broadcastMatchingResult()");
    }

    /***
     * Actual send method that creates a thread to establish a TCP connection and send a payload
     */
    private void sendMessage() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN P2PCommsManager.sendMessage()");
        if (thisDeviceActsAsServer) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Polling a message from the queue");
            Message message = pendingMessagesToSendQueue.poll();
            if (message == null) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Message is null");
                Log.d(WIFI_P2P_DEBUG_LABEL, "END P2PCommsManager.sendMessage()");
                return;
            }
            Log.d(WIFI_P2P_DEBUG_LABEL, "Message: " + message);
            if (message.getMessageType() == MESSAGE_TYPES.MATCHING_RESULT) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Sending MATCHING_RESULT.");
                new Thread(new SendMessageTask(message)).start();
            } else if (message.getMessageType() == MESSAGE_TYPES.JOURNEY_MATCH_REQUEST) {
                //don't send message but put it in local data structure
                //interacting with matching
                p2pMatching.addJourneyRequest((JourneyRequest) message.getPayload());
            }
        } else {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Polling a message from the queue");
            Message message = pendingMessagesToSendQueue.poll();
            if (message == null) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Message is null");
                Log.d(WIFI_P2P_DEBUG_LABEL, "END P2PCommsManager.sendMessage()");
                return;
            } else {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Message: " + message);
            }
            if (message.getMessageType() == MESSAGE_TYPES.JOURNEY_MATCH_REQUEST) {
                if (message.getDestinationIP() == null) {
                    //This means the message was added to the queue before the WiFiP2P process run
                    //Thus, it is ok to replace the destinationIP address, with the serverIPAddress
                    message.setDestinationIP(this.serverIPAddress);
                }
                //Send your journey preferences to the server
                Log.d(WIFI_P2P_DEBUG_LABEL, "Sending JOURNEY_MATCH_REQUEST.");
                new Thread(new SendMessageTask(message)).start();
            }
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "END P2PCommsManager.sendMessage()");
    }

    public void setServerIPAddress(String serverIPAddress) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "Setting serverIPAddress to: " + serverIPAddress);
        this.serverIPAddress = serverIPAddress;
    }

    public void notifyConnected(boolean thisDeviceConnected) {
        //Only the first time a group is formed, start a TCP receiver thread
        this.thisDeviceConnected = thisDeviceConnected;
        if (receiverThread == null) {
            //Start a thread to create a TCP server socket to listen for messagesReceived
            receiverThread = new Thread(new ReceiveMessageTask(this));
            receiverThread.start();
        }
    }

    public void notifyDeviceRole(boolean thisDeviceActsAsServer) {
        this.thisDeviceActsAsServer = thisDeviceActsAsServer;
        //TODO
        // clean list
    }

    public void notifyEvaluateSending() {
        if (this.thisDeviceConnected) {
            sendMessage();
        }
    }

    public void notifyMessageReceived(Message message) {
        if (message.getMessageType() == MESSAGE_TYPES.JOURNEY_MATCH_REQUEST) {
            if (this.thisDeviceActsAsServer) {
                JourneyRequest journeyRequest = (JourneyRequest) message.getPayload();
                Log.d(WIFI_P2P_DEBUG_LABEL, "Adding to the clients hashmap (key, value):" + journeyRequest.getUser().getLogin() + ", " + message.getOriginIP());
                clients.put(journeyRequest.getUser().getLogin(), message.getOriginIP());
                p2pMatching.addJourneyRequest(journeyRequest);
            }
        } else if (message.getMessageType() == MESSAGE_TYPES.MATCHING_RESULT) {
            //Raise local broadcast to update interface with the match result
            Intent intent = new Intent(Constants.JOURNEY_MATCH_RESULT_INTENT_ACTION);
            intent.putExtra(Constants.JOURNEY_MATCH_RESULT_INTENT_ACTION_PARCELABLE_KEY, (Parcelable) message.getPayload());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    public String getMACAddress() {
        return networkManager.getThisDeviceMACAddress();
    }

    @Override
    public void onResume() {
        networkManager.onResume();
    }

    @Override
    public void onPause() {
        networkManager.onPause();
    }

    @Override
    public void onStop() {
        networkManager.onStop();
    }

    @Override
    public void onDestroy() {
        networkManager.onDestroy();
        /*TODO: find a way to clean up threads
        //Cleaning up the Network Manager Monitor Thread
        if (networkManagerMonitorThread != null && networkManagerMonitorThread.isAlive()) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Stopping Network Manager Monitor Thread");
            //stop() on Thread probably won't work
            networkManagerMonitorThread.stop();
        }
        */
    }
}