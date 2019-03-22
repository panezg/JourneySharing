package org.cs7cs3.team7.wifidirect;


import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

public class NetworkManagerMonitor implements Runnable {
    NetworkManager networkManager;
    private int notInGroupCount = 0;
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI_MONITOR";

    public NetworkManagerMonitor(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        try {
            //Giving an initial sleep to avoid missing on events
            Thread.sleep(2000);
            while (true) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "NetworkManagerMonitor tick!");
                if (networkManager.isThisDevicePartOfGroup()) {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "This device is already part of a group");
                    notInGroupCount = 0;
                    //I think call below is unnecessary
                    //networkManager.wifiP2pManager.requestGroupInfo(networkManager.wifiP2pChannel, networkManager);
                    //networkManager.doRepeatedTasks();

                } else {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "This device is not part of a group yet");
                    notInGroupCount++;
                    Log.d(WIFI_P2P_DEBUG_LABEL, "notInGroupCount: " + notInGroupCount);
                    //You could be part of a group, your own group but still would need to timeout somehow and tell user no match
                    if (notInGroupCount > 5) {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "The count of retries has been exceeded, i.e., this device has not been part of a group for a long time.");
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Either there is no other device this device can connect to or a problem has occurred.");
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Probably inform the P2PCommsManager to raise a local broadcast to update the UI and tell the user there are no users around");
                        //Inform  P2PCommsManager
                        return;
                        //Probably inform the P2PCommsManager to raise a local broadcast to update the UI and kill this thread

                        //Log.d(WIFI_P2P_DEBUG_LABEL, "Calling NetworkManager.reset()");
                        //In some cases, it seems that even if one device can detect another device
                        //and can form a group with it, the second device cannot detect that the grouped
                        //was formed
                        //networkManager.reset();
                    }
                    else {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling NetworkManager.initiateWiFiP2PGroupFormation from NetworkManagerMonitor");
                        networkManager.initiateWiFiP2PGroupFormation();
                    }
                }
                //Check the status of network formation every couple of seconds
                Thread.sleep(Constants.NETWORK_MANAGER_MONITOR_WAITING_PERIOD);
            }
        } catch (Exception e) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Some exception occurred in NetworkManagerMonitor. Check errors");
            e.printStackTrace();
        }
    }
}