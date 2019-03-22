package org.cs7cs3.team7.wifidirect;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.util.ArrayList;
import java.util.Collection;

public class NetworkManager implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.GroupInfoListener, INetworkManager {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI";

    private WifiManager wifiManager;
    protected WifiP2pManager wifiP2pManager;
    protected WifiP2pManager.Channel wifiP2pChannel;
    private WiFiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
    private IntentFilter intentFilter;
    private Context context;
    private P2PCommsManager commsManager;
    private String thisDeviceMACAddress;

    //WiFi P2P group state variables
    private boolean isWifiP2pEnabled = false;
    private boolean isThisDevicePartOfGroup = false;
    private boolean isThisDeviceAttemptingToJoinGroup = false;
    private boolean isThisDeviceGroupOwner = false;


    public NetworkManager(Context context, P2PCommsManager commsManager) {
        this.context = context;
        this.commsManager = commsManager;
        //Enabling and resetting WiFi
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //wifiManager.setWifiEnabled(true);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        this.thisDeviceMACAddress = wifiInfo.getMacAddress();

        //Calls to wifiManager.getConnectionInfo() and wifiManager.getWifiState() are not useful
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);

        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WiFiP2pManager.initialize()");
        wifiP2pChannel = wifiP2pManager.initialize(context, Looper.getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Log.d(WIFI_P2P_DEBUG_LABEL, "The channel to the framework has been disconnected. Application could try re-initializing using initialize()");
            }
        });

        Log.d(WIFI_P2P_DEBUG_LABEL, "Creating WiFiDirectBroadcastReceiver");
        wifiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, wifiP2pChannel, this);
        Log.d(WIFI_P2P_DEBUG_LABEL, "Creating intent filter for WiFi P2P events");
        intentFilter = new IntentFilter();
        //To capture events related to WiFi P2P service
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        //To capture events related to peers being available nearby
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        //To capture events related to connecting to a group or peer
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        //To capture events related to status changes of this device
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }


    public void onResume() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onResume() of NetworkManager");
        Log.d(WIFI_P2P_DEBUG_LABEL, "Registering WiFi Direct Broadcast Receiver with the intent filter");
        context.registerReceiver(wifiDirectBroadcastReceiver, intentFilter);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onResume() of NetworkManager");
    }

    public void onPause() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onPause() of NetworkManager");
        Log.d(WIFI_P2P_DEBUG_LABEL, "Unregistering WiFi Direct Broadcast Receiver");
        context.unregisterReceiver(wifiDirectBroadcastReceiver);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onPause() of NetworkManager");
    }


    public void onStop() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onStop() of NetworkManager");
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onStop() of NetworkManager");
    }

    public void onDestroy() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onDestroy() of NetworkManager");
        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WiFiP2pManager.removeGroup(), which could help handle disconnection scenarios");
        wifiP2pManager.removeGroup(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Invocation of WiFiP2pManager.removeGroup() successful");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Invocation of WiFiP2pManager.removeGroup() failed. Reason: " + reason);
            }
        });

        //Cleaning up the Network Manager Monitor Thread
        /*
        if (networkManagerMonitorThread != null && networkManagerMonitorThread.isAlive()) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Stopping Network Manager Monitor Thread");
            //stop() on Thread probably won't work
            networkManagerMonitorThread.stop();
        }
        */
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onDestroy() of NetworkManager");
    }

    public void initiateWiFiP2PGroupFormation() {
        isThisDevicePartOfGroup = false;
        isThisDeviceGroupOwner = false;

        //routing = new Routing();

        //shouldn't find peers always be executed by the NetworkManagerMonitorThread?
        //create a thread that tries to find peers if it is not connected to a GO/

        findPeers();
    }

    private void findPeers() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN findPeers()");

        if (!isWifiP2pEnabled) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "isWifiP2pEnabled is false");
            Log.d(WIFI_P2P_DEBUG_LABEL, "So, either the WIFI_P2P_STATE_CHANGED_ACTION on event has not been captured yet or " +
                    "probably the WiFiManager on and off didn't work");
            //TODO: How to handle when the off event comes later than the click on the 'Search' button
            Log.d(WIFI_P2P_DEBUG_LABEL, "Cycle enable/disable WiFi/P2P via OS");
            Utility.toast("Cycle enable/disable WiFi/P2P via OS", context);
            return;
        }

        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling wifiP2pManager.discoverPeers()");
        //The discovery process involves scanning for available Wi-Fi P2P peers for the purpose of establishing a connection or forming a group
        //This is an async call that returns after passing orders to thee lower level WiFi P2P framework
        //This call should eventually result on a WIFI_P2P_PEERS_CHANGED_ACTION event captured by WiFiDirectBroadcastReceiver in an asynchronous fashion
        //How long the discovery process runs, and when it times out is not clear. It appears part of the behavior varies per API level or device manufacturer
        //Sometimes the discovery process remains active until a connection is initiated or a P2P group is formed
        //Issuing the same call in a short period of time tends to fail, but multiple calls in a period of tens of seconds seem to be safe
        wifiP2pManager.discoverPeers(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Invocation of WiFiP2pManager.discoverPeers() successful");
            }

            @Override
            public void onFailure(int reason) {
                Utility.toast("Failed to invoke WiFiP2pManager.discoverPeers(). Reason: " + reason, context);
                Log.d(WIFI_P2P_DEBUG_LABEL, "Failed to invoke WiFiP2pManager.discoverPeers(). Reason: " + reason);
            }
        });
        Log.d(WIFI_P2P_DEBUG_LABEL, "END findPeers()");
    }

    @Override
    /***
     * Implementing PeersListListener interface method to process the list of available devices nearby
     */
    public void onPeersAvailable(WifiP2pDeviceList list) {
        //This method is not directly called by application code. Instead, this method acts as a callback and
        //is called by the framework eventually as a result of a call to WiFiP2pManager.requestPeers() from
        //the WIFI_P2P_PEERS_CHANGED_ACTION handler on WiFiDirectBroadcastReceiver
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onPeersAvailable()");
        if (isThisDeviceAttemptingToJoinGroup) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "This device is already trying to join a WiFi P2P group. So, returning early from method.");
            Log.d(WIFI_P2P_DEBUG_LABEL, "END onPeersAvailable()");
            return;
        }
        if (isThisDevicePartOfGroup) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "This device is already part of a WiFi P2P group. So, returning early from method.");
            Log.d(WIFI_P2P_DEBUG_LABEL, "END onPeersAvailable()");
            return;
        }

        Log.d(WIFI_P2P_DEBUG_LABEL, "Peer list size : " + list.getDeviceList().size());
        if (list.getDeviceList().size() == 0) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "This peer list size is empty. So, returning early from method.");
            Log.d(WIFI_P2P_DEBUG_LABEL, "END onPeersAvailable()");
            return;
        }
        //as reported by Android
        ArrayList<WifiP2pDevice> peerList = new ArrayList<WifiP2pDevice>(list.getDeviceList());

        boolean isThereAtLeastOnePhoneDevice = false;
        boolean doesGOExist = false;
        WifiP2pDevice wifiP2PGO = null;
        WifiP2pDevice wifiP2PWithGreatestMAC = null;
        String greatestMACAddress = Constants.THIS_DEVICE_MAC_ADDRESS;

        //process the list and look for a group owner or the phone device with largest MAC
        for (WifiP2pDevice peer : peerList) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Device Found in onPeersAvailable " + peer.deviceAddress + " " + peer.deviceName);
            Log.d(WIFI_P2P_DEBUG_LABEL, "is Peer Group Owner? = " + peer.isGroupOwner());
            Log.d(WIFI_P2P_DEBUG_LABEL, "deviceStatus interpretation: CONNECTED = 0, INVITED = 1, FAILED = 2, AVAILABLE = 3, UNAVAILABLE = 4");
            Log.d(WIFI_P2P_DEBUG_LABEL, "What is the status of the peer discovered? = " + peer.status);
            Log.d(WIFI_P2P_DEBUG_LABEL, "device primary device type: " + peer.primaryDeviceType);
            Log.d(WIFI_P2P_DEBUG_LABEL, "device secondary device type: " + peer.secondaryDeviceType);
            if (!isDeviceAPhone(peer)) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "skipping non-phone device");
                continue;
            }
            isThereAtLeastOnePhoneDevice = true;
            //if (peer.status == WifiP2pDevice.AVAILABLE) {
            if (peer.isGroupOwner()) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Group owner found among list of available devices nearby");
                wifiP2PGO = peer;
                doesGOExist = true;
                break;
            }

            if (wifiP2PWithGreatestMAC == null) {
                wifiP2PWithGreatestMAC = peer;
            } else {
                wifiP2PWithGreatestMAC = maxByMAC(wifiP2PWithGreatestMAC, peer);
            }
        }

        if (!isThereAtLeastOnePhoneDevice) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "It seems there are only non-phone device(s) available; skipping the next steps of onPeersAvailable()");
            Log.d(WIFI_P2P_DEBUG_LABEL, "END onPeersAvailable()");
            return;
        }

        if (doesGOExist) {
            if (wifiP2PGO.status == WifiP2pDevice.AVAILABLE) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "This device should try connecting to Group Owner that is available");
                //Connect directly to the group owner. Our tests have shown that calling WiFiP2pManager
                //to connect to the group owner doesn't break the current group or changes which
                //device acts as group owner
                this.connect(new DeviceDTO(wifiP2PGO.deviceAddress, wifiP2PGO.deviceName, wifiP2PGO.status));
            } else if (wifiP2PGO.status == WifiP2pDevice.CONNECTED) {
                //This means that from the POV of this device, the status of the group owner is connected
                //which means this device is part of a group managed by the group owner
                Log.d(WIFI_P2P_DEBUG_LABEL, "Skip more analysis since you are already part of a group, and connected to the group owner");
            }
        } else {
            //If the device with the greatest MAC address is not this device, but one in the peer list, and
            //if such device's status is available, then wait for such device to create the group
            if (wifiP2PWithGreatestMAC.deviceAddress.compareTo(Constants.THIS_DEVICE_MAC_ADDRESS) > 0 &&
                    wifiP2PWithGreatestMAC.status == WifiP2pDevice.AVAILABLE) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "This device should wait for the device with largest MAC to form the group");
            }
            //If this device is the one with the greatest MAC address or if the device with the greatest
            //MAC address is not available, then proceed to form a group
            else {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Either this devices is the one with greatest MAC, or the peer with the greatest MAC is NOT AVAILABLE");
                Log.d(WIFI_P2P_DEBUG_LABEL, "So, this device will proceed to form a group");
                Log.d(WIFI_P2P_DEBUG_LABEL, "Calling NetworkManager.createGroup()");
                this.createGroup();
            }
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onPeersAvailable()");
    }

    /***
     * Returns the device with greatest MAC address from a pair of devices
     */
    private WifiP2pDevice maxByMAC(WifiP2pDevice device1, WifiP2pDevice device2) {
        int result = device1.deviceAddress.compareTo(device2.deviceAddress);

        if (result > 0) {
            return device1;
        } else if (result == 0) {
            return device1;
        } else {
            return device2;
        }
    }

    @Override
    /***
     * Allows the application to extract information about whether the group was formed,
     * if this device is the group owner, and the IP of the group owner
     * This method is called directly within the application. Instead, it is used as callback
     * after a call to WiFiP2pManager.requestConnectionInfo() during handling of a
     * WIFI_P2P_CONNECTION_CHANGED_ACTION event on WiFiDirectBroadcastReceiver
     */
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN onConnectionInfoAvailable()");
        Log.d(WIFI_P2P_DEBUG_LABEL, "info: " + info);

        //Reset flag to indicate you are no longer in the middle of the connection process, group creation process
        //or joining a group
        isThisDeviceAttemptingToJoinGroup = false;
        Log.d(WIFI_P2P_DEBUG_LABEL, "isThisDeviceAttemptingToJoinGroup flag set to " + isThisDeviceAttemptingToJoinGroup);

        isThisDevicePartOfGroup = info.groupFormed;
        Log.d(WIFI_P2P_DEBUG_LABEL, "isThisDevicePartOfGroup flag set to " + isThisDevicePartOfGroup);
        if (info.groupFormed) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Got the IP: " + info.groupOwnerAddress.getHostAddress());
            commsManager.setServerIPAddress(info.groupOwnerAddress.getHostAddress());

            isThisDeviceGroupOwner = info.isGroupOwner;
            if (isThisDeviceGroupOwner) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "This device will act as the group owner/server for peers");
                Utility.toast("This device will act as the group owner/server for peers", context);
            } else {
                Log.d(WIFI_P2P_DEBUG_LABEL, "This device will act as peer/client connecting to a group owner");
                Utility.toast("This device will act as peer/client connecting to a group owner", context);

            }
        }

        commsManager.notifyConnected(isThisDevicePartOfGroup);
        commsManager.notifyDeviceRole(isThisDeviceGroupOwner);
        commsManager.notifyEvaluateSending();
        Log.d(WIFI_P2P_DEBUG_LABEL, "END onConnectionInfoAvailable()");
    }

    /***
     * Establishes a WiFi P2P connection to a group owner, resulting on this device joining a group
     * Turns on and off the flag that allows the NetworkManager to track whether the process of
     * establishing a connection is ongoing
     * @param deviceDTO Contains the group owner device information
     */
    private void connect(final DeviceDTO deviceDTO) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN connect()");
        Log.d(WIFI_P2P_DEBUG_LABEL, "Connecting to device with name: " + deviceDTO.getDeviceName() + ", and device MAC address: " + deviceDTO.getDeviceMACAddress());

        //Set flag to indicate this device is trying to make connection to the group owner
        //Setting this flag early on to avoid possible onPeersAvailable() events to interrupt the connection process
        //Observed when Fire device connects third
        isThisDeviceAttemptingToJoinGroup = true;

        Utility.toast("Connecting to device with name: " + deviceDTO.getDeviceName() + ", and device address: " + deviceDTO.getDeviceMACAddress(), context);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceDTO.getDeviceMACAddress();
        //This is configuration of Push Button Connectivity when connecting to WPS
        //Class deprecated on API 28, because of security vulnerabilities but remains the only way
        //to make this work
        config.wps.setup = WpsInfo.PBC;
        //Indicates that this device doesn't want to take over group owner responsibilities
        config.groupOwnerIntent = 1;

        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WiFiP2pManager.connect()");
        //WiFiP2pManager.connect() method sends a request for connection
        //Similar to connecting to a WiFi network. The receiver of the request, in this case the group
        //owner needs to respond, and in some cases, it might require user approval on the device acting
        //as group owner
        wifiP2pManager.connect(wifiP2pChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                isThisDeviceAttemptingToJoinGroup = true;
                Log.d(WIFI_P2P_DEBUG_LABEL, "Successful invocation of WiFiP2pManager.connect() to device with MAC address: " + deviceDTO.getDeviceMACAddress());
                Utility.toast("Successfully requested connection to: " + deviceDTO.getDeviceMACAddress(), context);
            }

            @Override
            public void onFailure(int reasonCode) {
                isThisDeviceAttemptingToJoinGroup = false;
                Log.d(WIFI_P2P_DEBUG_LABEL, "Failed to invoke WiFiP2pManager.connect() for device with MAC address: " + deviceDTO.getDeviceMACAddress());
                Utility.toast("Failed to get connection to: " + deviceDTO.getDeviceMACAddress() + ", because of reasonCode: " + reasonCode, context);
            }
        });
        Log.d(WIFI_P2P_DEBUG_LABEL, "END connect()");
    }

    /***
     * Creates a WiFi P2P group
     */
    private void createGroup() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN createGroup()");

        isThisDeviceAttemptingToJoinGroup = true;

        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WiFiP2pManager.createGroup()");
        //WiFiP2pManager.createGroup() method will result in creation of a group, and events
        //WIFI_P2P_THIS_DEVICE_CHANGED_ACTION and WIFI_P2P_CONNECTION_CHANGED_ACTION being raised by
        //the framework and captured by WiFiDirectBroadcastReceiver
        //WIFI_P2P_THIS_DEVICE_CHANGED_ACTION will indicate that this device's status is connected
        //while WIFI_P2P_CONNECTION_CHANGED_ACTION will indicate that a network handled by this device
        //was created and indicate that this device is the group owner. It will also probably
        //show that the list of devices part of the group is empty, for the first WIFI_P2P_CONNECTION_CHANGED_ACTION event
        wifiP2pManager.createGroup(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Invocation of WiFiP2pManager.createGroup() successful");
                Utility.toast("Successfully requested group formation", context);
                isThisDeviceAttemptingToJoinGroup = true;
            }

            @Override
            public void onFailure(int reason) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Failed to invoke WiFiP2pManager.createGroup(). Reason: " + reason);
                Utility.toast("Failed to form group ", context);
                isThisDeviceAttemptingToJoinGroup = false;
            }
        });
        Log.d(WIFI_P2P_DEBUG_LABEL, "END createGroup()");
    }

    /***
     * We need a list of devices that are not phones
     * @param peer
     * @return
     */
    private boolean isDeviceAPhone(WifiP2pDevice peer) {
        //temporary provision to ignore devices such as printers and other phones
        //TODO: more analysis is needed to decide which devices to consider
        if (peer.deviceName.contains("HP")) return false;
        if (peer.deviceName.contains("DELL")) return false;
        if (peer.deviceName.contains("aRM2070")) return false;
        if (peer.deviceName.contains("Iceberg") || peer.deviceName.contains("iceberg"))
            return false;
        if (!peer.primaryDeviceType.contains("0050F204")) {
            return false;
        }
        //if(! peer.deviceName.startsWith("null")) return true;
        //if(!peer.primaryDeviceType.contains("0050F204")) return true;
        return true;
    }

    public boolean isThisDevicePartOfGroup() {
        return isThisDevicePartOfGroup;
    }

    public boolean isThisDeviceGroupOwner() {
        return isThisDeviceGroupOwner;
    }

    @Override
    public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
        Log.d("JINCHI_MONITOR", "**********Logging group info**********");

        //sometimes on old device it it shown that group is formed, but in reality it is not.
        if (wifiP2pGroup == null) return;

        Log.d("JINCHI_MONITOR", "wifiP2pGroup: " + wifiP2pGroup);

        WifiP2pDevice groupOwner = wifiP2pGroup.getOwner();
        if (groupOwner == null) {
            Log.d("JINCHI_MONITOR", "No group owner found in onGroupInfoAvailable. So you are no longer in group. Resetting isThisDevicePartOfGroup");
            isThisDevicePartOfGroup = false;
            return;
        }

        if (groupOwner.deviceAddress.equals(Constants.THIS_DEVICE_MAC_ADDRESS)) {
            Log.d("JINCHI_MONITOR", "You are the owner of the group");
            Log.d("JINCHI_MONITOR", "List of the peers in the group");
            Collection<WifiP2pDevice> peerList = wifiP2pGroup.getClientList();
            if (peerList.size() == 0) {
                //This might not be true
                Log.d("JINCHI_MONITOR", "No peers found in onGroupInfoAvailable. So you are no longer in group. Resetting isThisDevicePartOfGroup");
                isThisDevicePartOfGroup = false;
                isThisDeviceGroupOwner = false;
                return;
            }

            int count = 1;
            for (WifiP2pDevice peer : peerList) {
                Log.d("JINCHI_MONITOR", "" + count++);
                Log.d("JINCHI_MONITOR", "Device Address: " + peer.deviceAddress);
                Log.d("JINCHI_MONITOR", "Device Name: " + peer.deviceName);
                Log.d("JINCHI_MONITOR", "Device Primary Type: " + peer.primaryDeviceType);
                Log.d("JINCHI_MONITOR", "Device Status: " + peer.status);

            }
        } else {
            Log.d("JINCHI_MONITOR", "You are NOT the owner of the group");
            Log.d("JINCHI_MONITOR", "Group owner information");

            Log.d("JINCHI_MONITOR", "Group owner Address: " + groupOwner.deviceAddress);
            Log.d("JINCHI_MONITOR", "Group owner Name: " + groupOwner.deviceName);
            Log.d("JINCHI_MONITOR", "Group owner Primary Type: " + groupOwner.primaryDeviceType);
            Log.d("JINCHI_MONITOR", "Group owner Status: " + groupOwner.status);
        }


        Log.d("JINCHI_MONITOR", "**********Logging group info ends**********");
    }

    public String getThisDeviceMACAddress() {
        return this.thisDeviceMACAddress;

    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    /*
    public void sendMessage2(Message message, boolean sendingTripDetails) {
        sendMessage = message;
        Log.d(WIFI_P2P_DEBUG_LABEL, "In send message: message= " + message + ", sendTripDetails= " + sendingTripDetails);
        if (isThisDevicePartOfGroup) {
            if (isThisDeviceGroupOwner) {
                if (sendingTripDetails) {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "If you are group owner then don't send trip details to anyone. Just record them.");
                    //routing.addJourneyRequest(message);
                } else {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "Broadcasting the message to all the peers as I am the group owner");
                    /*
                    for (Map.Entry<String, String> entry : macToIpMapping.entrySet()) {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Broadcasting to " + entry.getValue());
                        new Thread(new SendMessageTask(entry.getValue(), message)).start();
                    }
                }
            } else {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Sending the message to the group owner.");
                new Thread(new SendMessageTask(groupOwnerIPAddress, message)).start();
                //new SendMessageTask().execute(groupOwnerIPAddress, messageText);
            }
        } else {
            Log.d(WIFI_P2P_DEBUG_LABEL, "You are not in the network. reset netwrok initiated");
        }
    }
  */
    /*
    void doRepeatedTasks() {
        if (isThisDeviceGroupOwner) {
            //if you are group owner, check if routing is done and if group is ready then broadcast the groups
            if (routing.isMatchDone()) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "Groups are ready, so broadcast them to peers");
                Message broadcastMessage = new Message();
                broadcastMessage.setList(routing.getJourneyRequests());
                sendMessage(broadcastMessage, false);
            }
        } else {
            //broadcast your search regularly
            sendMessage(sendMessage, true);
        }
    }
    */
}
