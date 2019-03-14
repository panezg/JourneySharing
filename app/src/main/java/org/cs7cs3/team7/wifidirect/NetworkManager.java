package org.cs7cs3.team7.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NetworkManager implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.GroupInfoListener {
    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifiP2pChannel;
    WiFiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
    IntentFilter intentFilter;
    private boolean isWDConnected = false;
    private boolean isWifiP2pEnabled = false;
    boolean connectedToGroup=false;
    boolean tryingToMakeConnection=false;
    private boolean mIGroupOwner=false;
    private String groupOwnerAddress;
    public String deviceName;

    private Map<String,String> macToIpMapping=new HashMap<>();
    private Context context;
    private NetworkManagerMonitor networkManagerMonitor;
    private Thread networkManagerMonitorThread;
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public NetworkManager(Context context){
        this.context=context;
        //Requires change from min API 22 to 23
        //wifiManager = (WifiManager) context.getSystemService(WifiManager.class);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);

        Log.d("JINCHI", "Calling WiFiManager.getConnectionInfo()");
        WifiInfo info = wifiManager.getConnectionInfo();
        Log.d("JINCHI", "Calling WiFiManager.getWifiState() returns: " + wifiManager.getWifiState());
        Log.d("JINCHI", "Calling WiFiManager.disconnect()");

        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);


        //Requires change from min API 22 to 23
        //wifiP2pManager = (WifiP2pManager) getSystemService(WifiP2pManager.class);
        //Strin s=Build.MODEL;
        Log.d("JINCHI", "Calling WiFiP2pManager.initialize()");
        wifiP2pChannel = wifiP2pManager.initialize(context, Looper.getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Log.d("JINCHI", "The channel to the framework has been disconnected. Application could try re-initializing using initialize()");
            }
        });

        wifiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, wifiP2pChannel, this);

        intentFilter = new IntentFilter();
        //No at use yet
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        //Indicates a new WiFi devices is located
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        //Indicates that a WiFi connection to a device changed (success, or failure)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        //No at use yet
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }


    public void onResume() {
        //super.onResume();
        Log.d("JINCHI", "in onResume() of NetworkManager");

        //system call
        Log.d("JINCHI", "WiFi Direct Broadcast receiver registered with intent filter");
        context.registerReceiver (wifiDirectBroadcastReceiver, intentFilter);
    }

    public void onPause() {
        //super.onPause();
        Log.d("JINCHI", "in onPause() of NetworkManager");
        context.unregisterReceiver(wifiDirectBroadcastReceiver);
    }


    public void onStop() {
        //super.onStop();
        Log.d("JINCHI", "in onStop() of NetworkManager");
    }

    public void onDestroy() {
        //TODO: Need to review this
        //super.onDestroy();
        Log.d("JINCHI", "in onDestroy() of NetworkManager");
        Log.d("JINCHI", "calling WiFiP2pManager.removeGroup() of NetworkManager");
        Log.d("JINCHI", "in theory this is for disconnect");
        wifiP2pManager.removeGroup(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("JINCHI", "Invocation of WiFiP2pManager.removeGroup() successful");
            }
            @Override
            public void onFailure(int i) {
                Log.d("JINCHI", "Invocation of WiFiP2pManager.removeGroup() failed. Error: " + i);
            }
        });

        //killing the netwrok manager monitor thread
        if(networkManagerMonitorThread!=null && networkManagerMonitorThread.isAlive()){
            Log.d("JINCHI", "stopping network manager monitor thread");
            networkManagerMonitorThread.stop();
        }
        Log.d("JINCHI", "in onDestroy() of NetworkManager");
    }

    public void changeDeviceName(int flag){
        Log.e("JINCHI", "changing device name");
        if(flag==0){
            setDeviceName(this.deviceName+"-on");
        }
        else if(flag==1){
            setDeviceName(this.deviceName+"-off");
        }
    }

    private void setDeviceName(String deviceName){
        try {
            Log.e("JINCHI", "1");
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = wifiP2pManager.getClass().getMethod(
                    "setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);
            Log.e("JINCHI", "2");
            Object arglist[] = new Object[3];
            arglist[0] = wifiP2pChannel;
            arglist[1] = deviceName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d("setDeviceName succeeded", "true");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d("setDeviceName failed", "true");
                }
            };
            setDeviceName.invoke(wifiP2pManager, arglist);
            Log.e("JINCHI", "3");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void initiateNetworkActivity(){
        findPeers();

        //This thread is to monitor network conditions in timely manner and take proper actions(eg. reset network, log group owner)
        networkManagerMonitor=new NetworkManagerMonitor(this);
        networkManagerMonitorThread=new Thread(networkManagerMonitor);
        Log.d("JINCHI", "starting network manager monitor thread");
        networkManagerMonitorThread.start();
    }

    //TODO: Why Fire Tablet has different logged messages than Xiomi?
    private void findPeers() {
        Log.d("JINCHI", "onside find peers");
        //At the moment, isWDConnected is always false, so !isWDConnected is always true

        if (!isWifiP2pEnabled) {
            Log.d("JINCHI", "Enable P2P from action bar button above or system settings");
            Utility.toast("Enable P2P from action bar button above or system settings",context);
            return;
        }

        if (!isWDConnected) {
            Log.d("JINCHI", "Initiate peer discovery");
            //will trigger a WIFI_P2P_PEERS_CHANGED_ACTION event captured by WiFiDirectBroadcastReceiver
            //A discovery process involves scanning for available Wi-Fi peers for the purpose of establishing a connection.
            //Async call that immediately returns, and handles cases through callbacks
            //The discovery remains active until a connection is initiated or a p2p group is formed.
            //It seems the call blocks
            wifiP2pManager.discoverPeers(wifiP2pChannel, new WifiP2pManager.ActionListener() {
                //Useless callbacks, don't provide info
                @Override
                public void onSuccess() {
                    Log.d("JINCHI", "Invocation of WiFiP2pManager.discoverPeers() successful");
                }

                @Override
                public void onFailure(int reasonCode) {
                    Utility.toast("Failed to invoke WiFiP2pManager.discoverPeers(). Error code: " + reasonCode, context);
                    Log.d("JINCHI", "Failed to invoke WiFiP2pManager.discoverPeers(). Error code: " + reasonCode);
                }
            });
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList list) {

        //Execution resumes via callback due to manager.requestPeers() from WIFI_P2P_PEERS_CHANGED_ACTION handler on WiFiDirectBroadcastReceiver
        Log.d("JINCHI", "onPeersAvailable()");

        if(tryingToMakeConnection) {
            Log.d("JINCHI", "Already trying to make connection to some other peer. So, skipping further method until flag is unset.");
            return;
        }
        if(connectedToGroup) {
            Log.d("JINCHI", "skipping the method as you are already connected to the group");
            return;
        }
        Log.d("JINCHI", "Peer list size : " + list.getDeviceList().size());
        if(list.getDeviceList().size() == 0) {
            return;
        }
        //as reported by Android
        ArrayList<WifiP2pDevice> peerList = new ArrayList<WifiP2pDevice>(list.getDeviceList());

        boolean isThereAtLeastOnePhoneDevice=false;
        boolean doesGOExist = false;
        WifiP2pDevice peerGO = null;
        DeviceDTO deviceGO = new DeviceDTO();
        DeviceDTO deviceLargestMAC = new DeviceDTO();
        deviceLargestMAC.setDeviceAddress(Constants.myMACAddress);

        //election by MAC address should be only among availables

        for (WifiP2pDevice peer : peerList){
            DeviceDTO device = new DeviceDTO();
            device.setDeviceAddress(peer.deviceAddress);
            device.setDeviceName(peer.deviceName);
            device.status = peer.status;

            Log.d("JINCHI", "Device Found in onPeersAvailable "+device.getDeviceAddress()+" "+device.getDeviceName());
            Log.d("JINCHI", "is Peer Group Owner? = " + peer.isGroupOwner());
            Log.d("JINCHI", "deviceStatus interpretation: CONNECTED = 0, INVITED = 1, FAILED = 2, AVAILABLE = 3, UNAVAILABLE = 4");
            Log.d("JINCHI", "What is the status of the peer discovered? = " + peer.status);
            Log.d("JINCHI", "device primary device type: " + peer.primaryDeviceType);
            Log.d("JINCHI", "device secondary device type: " + peer.secondaryDeviceType);
            if(isNonPhoneDevice(peer)){
                Log.d("JINCHI", "skipping non-phone device");
                continue;
            }
            isThereAtLeastOnePhoneDevice=true;
            //if (peer.status == WifiP2pDevice.AVAILABLE) {
            if (peer.isGroupOwner()) {
                peerGO = peer;
                doesGOExist = true;
                deviceGO.setDeviceAddress(peer.deviceAddress);
                deviceGO.setDeviceName(peer.deviceName);
                break;
            }
            deviceLargestMAC = compareMACAddress(deviceLargestMAC, device);
            //}
        }

        if(! isThereAtLeastOnePhoneDevice) {
            Log.d("JINCHI", "It seems there are only non-phone device(s) available; skipping the next steps in onPeersAvailable");
            return;
        }

        if (doesGOExist) {
            if (peerGO.status == WifiP2pDevice.AVAILABLE){
                Log.d("JINCHI", "This device should try connecting to Group Owner who is available");
                this.connect(deviceGO);
            }
            else if (peerGO.status == WifiP2pDevice.CONNECTED) {
                Log.d("JINCHI", "Skip more analysis since you are already connected");
            }
        } else {
            if (!deviceLargestMAC.getDeviceAddress().equals(Constants.myMACAddress) && deviceLargestMAC.status == WifiP2pDevice.AVAILABLE) {
                Log.d("JINCHI", "This device should wait for group formation");
            }
            else {
                //Log.d("JINCHI", "Either you are the one with highest MAC, or nobody else is AVAILABLE");
                Log.d("JINCHI", "This device should form a group");
                this.createGroup(deviceLargestMAC);
            }

        }

    }


    //Return the device with greatest MAC of both
    private DeviceDTO compareMACAddress(DeviceDTO device1, DeviceDTO device2) {
        int result = device1.getDeviceAddress().compareTo(device2.getDeviceAddress());

        if (result < 0) {
            return device1;
        }
        else if (result == 0) {
            return device1;
        }
        else {
            return device2;
        }
    }

    private boolean shouldConnectToDevice(DeviceDTO deviceDTO){
        if(deviceDTO.getDeviceAddress().compareTo(Constants.myMACAddress) <0)
            return true;
        return false;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d("JINCHI", "onConnectionInfoAvailable()");

        Log.d("JINCHI", "info: "+info);

        //reset flag to indcate you are no more in connection process
        tryingToMakeConnection=false;
        Log.d("JINCHI", "tryingToMakeConnection flag set to "+tryingToMakeConnection);

        //Execution resumes via callback due to manager.requestConnectionInfo() from WIFI_P2P_CONNECTION_CHANGED_ACTION handler on WiFiDirectBroadcastReceiver
        if(info.isGroupOwner) {

            Log.d("JINCHI", "You are the owner/server of peers");
            Utility.toast("You are the owner/server of peers",context);
            mIGroupOwner=true;
            groupOwnerAddress=null;

        }
        else{
            Log.d("JINCHI", "You are a client connected to a peer server");
            Utility.toast("You are a client connected to a peer server",context);
            mIGroupOwner=false;
            groupOwnerAddress=info.groupOwnerAddress.getHostAddress();
        }

        //only if you are connecting to group first time then only start the below threads for receiving messages and creating group-owner specific local broadcast listene r
        if(! (connectedToGroup && info.groupFormed)){
            //thread to start socket for receiving messages
            new Thread(new ReceiveMessageTask(context)).start();

            //only group owner needs to start local broadcast listener to listen for incoming messages and re-broadcast them again
            if(info.isGroupOwner) {
                //local broadcast-message receiver for group owner to listen to message sent from peers
                BroadcastReceiver messageReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String messageJson = intent.getStringExtra("message");
                        Log.d("JINCHI", "Local broadcast received in group-owner specific receiver: " + messageJson);
                        Message message = Utility.fromJson(messageJson);
                        macToIpMapping.put(message.getFromMAC(), message.getFromIP());
                        //send it to all the peers
                        sendMessage(message);
                    }
                };
                LocalBroadcastManager.getInstance(context).registerReceiver(messageReceiver, new IntentFilter("MESSAGE_RECEIVED"));
            }
        }

        //set variable to indicate you are already connected
        connectedToGroup=info.groupFormed;
        Log.d("JINCHI", "connectedToGroup flag set to "+connectedToGroup);
    }

    @Override
    public void onGroupInfoAvailable(WifiP2pGroup wifiP2pGroup) {
        Log.d("MONITOR", "**********Logging group info**********");

        //sometimes on old device it it shown that group is formed, but in reality it is not.
        if(wifiP2pGroup==null) return;

        WifiP2pDevice groupOwner=wifiP2pGroup.getOwner();
        if(groupOwner==null){
            Log.d("MONITOR", "No group owner found in onGroupInfoAvailable. So you are no longer in group. Resetting connectedToGroup");
            connectedToGroup=false;
            return;
        }

        if(groupOwner.deviceAddress.equals(Constants.myMACAddress)){
            Log.d("MONITOR", "You are the owner of the group");
            Log.d("MONITOR", "List of the peers in the group");
            Collection<WifiP2pDevice> peerList= wifiP2pGroup.getClientList();
            if(peerList.size()==0){
                Log.d("MONITOR", "No peers found in onGroupInfoAvailable. So you are no longer in group. Resetting connectedToGroup");
                connectedToGroup=false;
                mIGroupOwner=false;
                return;
            }

            int count=1;
            for(WifiP2pDevice peer:peerList){
                Log.d("MONITOR", ""+count++);
                Log.d("MONITOR", "Device Address: "+peer.deviceAddress);
                Log.d("MONITOR", "Device Name: "+peer.deviceName);
                Log.d("MONITOR", "Device Primary Type: "+peer.primaryDeviceType);
                Log.d("MONITOR", "Device Status: "+peer.status);

            }
        }
        else{
            Log.d("MONITOR", "You are NOT the owner of the group");
            Log.d("MONITOR", "Group owner information");

            Log.d("MONITOR", "Group owner Address: "+groupOwner.deviceAddress);
            Log.d("MONITOR", "Group owner Name: "+groupOwner.deviceName);
            Log.d("MONITOR", "Group owner Primary Type: "+groupOwner.primaryDeviceType);
            Log.d("MONITOR", "Group owner Status: "+groupOwner.status);
        }


        Log.d("MONITOR", "**********Logging group info ends**********");
    }

    private void connect(final DeviceDTO deviceDTO) {
        Log.d("JINCHI", "Connecting to device with name: " + deviceDTO.getDeviceName() + ", and device address: " + deviceDTO.getDeviceAddress());

        //set flag to indicate this device is trying to make connection
        //setting this flag on onSucess method below is not enough as wifiP2pManager.connect is asynchronous and in the meantime when callback could return to
        //onSuccess, onPeersAvaiable() can get invoked (observed when Fire device connects third)
        tryingToMakeConnection=true;

        Utility.toast("Connecting to device with name: " + deviceDTO.getDeviceName() + ", and device address: " + deviceDTO.getDeviceAddress(),context);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = deviceDTO.getDeviceAddress();

        //This is configuration of Push Button Connectivity when connecting to WPS. Class deprecated on API 28, because of security vulnerabilities
        config.wps.setup = WpsInfo.PBC;

        config.groupOwnerIntent = 1;

        //connect sends a request for connection (as connecting to a WiFi network, than peer needs to respond, and may require user approval

        Log.d("JINCHI", "Calling WiFiP2pManager.connect()");
        wifiP2pManager.connect(wifiP2pChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("JINCHI", "Invocation of WiFiP2pManager.connect() successful for device address: " + deviceDTO.getDeviceAddress());
                Utility.toast("Successfully requested connection to: " + deviceDTO.getDeviceAddress(),context);
                tryingToMakeConnection=true;
                Log.d("JINCHI", "tryingToMakeConnection flag is set to true");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.d("JINCHI", "Failed to invoke WiFiP2pManager.connect() for device address:: " + deviceDTO.getDeviceAddress());
                Utility.toast("Failed to get connection to: " + deviceDTO.getDeviceAddress() + ", because of reasonCode: " + reasonCode,context);
                tryingToMakeConnection=false;
                Log.d("JINCHI", "tryingToMakeConnection flag is set to false");
            }
        });
    }

    private void createGroup(final DeviceDTO deviceDTO) {
        Log.d("JINCHI", "in createGroup method");

        //TODO: add tryingToConnect like in connect()

        Log.d("JINCHI", "Calling WiFiP2pManager.createGroup()");
        wifiP2pManager.createGroup(wifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("JINCHI", "Invocation of WiFiP2pManager.createGroup() successful ");
                Utility.toast("Successfully requested group formation to: ",context);
                tryingToMakeConnection=true;
                Log.d("JINCHI", "tryingToMakeConnection flag is set to true");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("JINCHI", "Failed to invoke WiFiP2pManager.createGroup() ");
                Utility.toast("Failed to form group ",context);
                tryingToMakeConnection=false;
                Log.d("JINCHI", "tryingToMakeConnection flag is set to false");
            }
        });

    }

    void resetNetwork(){
        //currently only calling findPeers
        //TODO: in future other parameters' reset may be required
        findPeers();
    }


    private boolean isNonPhoneDevice(WifiP2pDevice peer){
        //temporary provision to ignore devices such as printers and other phoes
        //TODO: more analysis is needed to decide which devices to consider
        if(peer.deviceName.contains("HP")) return true;
        if(peer.deviceName.contains("DELL")) return true;
        if(peer.deviceName.contains("Iceberg") || peer.deviceName.contains("iceberg")) return true;
        //if(! peer.deviceName.startsWith("null")) return true;
        //if(!peer.primaryDeviceType.contains("0050F204")) return true;
        return false;
    }

    public void sendMessage(Message message){
        if(connectedToGroup) {
            if(mIGroupOwner){
                Log.d("JINCHI", "Broadcasting the message to all the peers as I am the group owner");
                Log.e("JINCHI", Thread.currentThread().getName()+" "+ Thread.currentThread().getId());
                for (Map.Entry<String, String> entry : macToIpMapping.entrySet()) {
                    Log.d("JINCHI", "Broadcasting to "+entry.getValue());
                    new Thread(new SendMessageTask(entry.getValue(),message)).start();
                }
            }
            else{
                Log.d("JINCHI", "Sending the message to the group owner. The owner shall broadcast it to other peers");
                new Thread(new SendMessageTask(groupOwnerAddress,message)).start();
                //new SendMessageTask().execute(groupOwnerAddress, messageText);
            }
        }
        else{
            Log.d("JINCHI", "Can't send the message over network as you are not in a group");
        }
    }

    /*private void startRegistration() {
        //  Create a string map containing information about your service.
        Map record = new HashMap();
        record.put("listenport", String.valueOf(8989));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        wifiP2pManager.addLocalService(wifiP2pChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("JINCHI", "Invocation of WiFiP2pManager.addLocalService() successful ");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d("JINCHI", "Failed to invoke WiFiP2pManager.addLocalService() ");
            }
        });
    }

    final HashMap<String, Object> buddies = new HashMap<String, Object>();

        private void discoverService() {


            WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
                @Override
                *//* Callback includes:
                 * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
                 * record: TXT record dta as a map of key/value pairs.
                 * device: The device running the advertised service.
                 *//*

                public void onDnsSdTxtRecordAvailable(
                        String fullDomain, Map record, WifiP2pDevice device) {
                    Log.d("JINCHI", "DnsSdTxtRecord available -" + record.toString());
                    Log.d("JINCHI",device.deviceAddress+" : "+record.get("buddyname"));
                    buddies.put(device.deviceAddress, record.get("buddyname"));
                }
            };

            WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
                @Override
                public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                    WifiP2pDevice resourceType) {

                    // Update the device name with the human-friendly version from
                    // the DnsTxtRecord, assuming one arrived.
                    Object o = buddies
                            .containsKey(resourceType.deviceAddress) ? buddies
                            .get(resourceType.deviceAddress) : resourceType.deviceName;
                    resourceType.deviceName=(String) o;
                    // Add to the custom adapter defined specifically for showing
                    // wifi devices.
                    Log.d("JINCHI", "3 params " + instanceName);
                    Log.d("JINCHI", "3 params " + registrationType);
                    Log.d("JINCHI", "3 params " + resourceType);

                }
            };

            wifiP2pManager.setDnsSdResponseListeners(wifiP2pChannel, servListener, txtListener);


        }*/



}

class NetworkManagerMonitor implements Runnable {
    NetworkManager netwrokManager;
    private int notInGroupTime=0;
    public NetworkManagerMonitor(NetworkManager netwrokManager){
        this.netwrokManager=netwrokManager;
    }

    @Override
    public void run() {
        try{
            while(true) {
                //check network conditions in timely manner
                Thread.sleep(Constants.NETWORK_MANAGER_MONITOR_WAITING_PERIOD);

                if (netwrokManager.connectedToGroup) {
                    Log.d("MONITOR", "You are Connected to group");
                    notInGroupTime=0;
                    netwrokManager.wifiP2pManager.requestGroupInfo(netwrokManager.wifiP2pChannel, netwrokManager);
                } else {
                    Log.d("MONITOR", "You are not connected to group");
                    notInGroupTime++;
                    if(notInGroupTime>2){
                        Log.d("MONITOR", "You are not in a group for long time.");
                        Log.d("MONITOR", "Either there is no one else to connect to or some problem has occurred. Netwrok reset is required (TODO:)");
                        //it seems in some cases even if a device can see the other device and can form the group with it, that other device cannot see the grouped formed.
                        netwrokManager.resetNetwork();
                    }
                }
            }
        }
        catch(Exception e){
            Log.d("MONITOR", "Some exception occured in NetworkManagerMonitor. CHeck errors");
            e.printStackTrace();
        }
    }
}