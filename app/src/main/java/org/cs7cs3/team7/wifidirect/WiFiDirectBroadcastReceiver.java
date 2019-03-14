/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cs7cs3.team7.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;


/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    //private MainActivity activity;
    private NetworkManager activity;
    private boolean hasWiFiOffBeenDetectedAtLeastOnce = false;
    private boolean haveSeenThisDeviceChangedEvent = false;
    private static final String TAG = "JINCHI";

    /**
     * @param manager  WifiP2pManager system service
     * @param channel  Wifi p2p channel
     * @param activity activity associated with the receiver
     */
    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
                                       NetworkManager activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "WiFiDirectBroadcastReceiver.onReceive() BEGIN");
        String action = intent.getAction();
        //Broadcast when Wi-Fi P2P is enabled or disabled on the device.
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_STATE_CHANGED_ACTION] BEGIN IF");
            // Check to see if Wi-Fi is enabled and notify appropriate activity

            //Check if WiFi P2P/direct is on or off
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct mode is enabled
                Log.d(TAG, "[WIFI_P2P_STATE_CHANGED_ACTION] WiFi P2P is on");
                activity.setIsWifiP2pEnabled(true);
            } else {
                Log.d(TAG, "[WIFI_P2P_STATE_CHANGED_ACTION] WiFi P2P is off");
                activity.setIsWifiP2pEnabled(false);
                hasWiFiOffBeenDetectedAtLeastOnce = true;
            }
            Log.d(TAG, "P2P state changed to: " + state + ". 1 means Disabled, 2 means Enabled");
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_STATE_CHANGED_ACTION] END IF");
        }
        //Broadcast when you call discoverPeers(). You usually want to call requestPeers() to get an updated list of peers if you handle this intent in your application
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_PEERS_CHANGED_ACTION] BEGIN IF");

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

            // Call WifiP2pManager.requestPeers() to get a list of current peers

            //GPV: Should not be null, check before on creation of receiver
            Log.d(TAG, "[WIFI_P2P_PEERS_CHANGED_ACTION] P2P peers changed");
            Log.d(TAG, "Is manager null? " + (manager == null));
            if (manager != null) {
                //GPV: Why make the activity, listener?
                //requestPeers() needs to be called to get the list. At this point, API only tells you there has been a change on the list of peers, e.g., new list from zero, new peer, etc.
                //raises a onPeersAvailable even captured by the activity because it implements interface
                Log.d(TAG, "hasWiFiOffBeenDetectedAtLeastOnce? " + hasWiFiOffBeenDetectedAtLeastOnce);
                if (hasWiFiOffBeenDetectedAtLeastOnce) {
                    Log.d(TAG, "Calling WifiP2pManager.requestPeers() from BroadcastReceiver");
                    manager.requestPeers(channel, activity);
                }
            }
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_PEERS_CHANGED_ACTION] END IF");
        }
        //When the other device we tried to connect in the step before, accepts the connection request, the connection state of such device changes
        //Broadcast when the state of the device's Wi-Fi connection changes.
        //Broadcast intent action indicating that the state of Wi-Fi p2p connectivity has changed.
        //Weird??
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_CONNECTION_CHANGED_ACTION] BEGIN IF");

            // Respond to new connection or disconnections

            if (manager == null) {
                return;
            }
            //provides the p2p connection info
            WifiP2pInfo p2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] p2pInfo (represents connection information about a Wi-Fi p2p group): " + p2pInfo.toString());
            //Provides the network info
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] networkInfo (describes the status of the network interface): " + networkInfo.toString());
            WifiP2pGroup wifiP2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
            //A p2p group consists of a single group owner and one or more clients
            Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] wifiP2pGroup (represents a Wi-Fi P2p group): " + wifiP2pGroup.toString());

//            if (p2pInfo != null && p2pInfo.groupOwnerAddress != null) {
//                String goAddress = Utility.getDottedDecimalIP(p2pInfo.groupOwnerAddress
//                        .getAddress());
//                boolean isGroupOwner = p2pInfo.isGroupOwner;
//            }

            if (networkInfo.isConnected()) {
                Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] Something happened 1 here");
                //Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] A peer device accepted your connection request");
                // we are connected with the other device, request connection
                // info to find group owner IP
                if (hasWiFiOffBeenDetectedAtLeastOnce) {
                    //TODO: GPV Not sure why invoked here
                    Log.d(TAG, "Calling WifiP2pManager.requestConnectionInfo() from BroadcastReceiver");
                    manager.requestConnectionInfo(channel, activity);
                }
            } else {
                Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] Something ELSE happened 1 here");
                // Log.d(TAG, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] A peer device disconnect from you");
                // It's a disconnect
                // activity.resetData();
            }

            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_CONNECTION_CHANGED_ACTION] END IF");
        }
        //Broadcast when a device's details have changed, such as the device's name
        //Broadcast intent action indicating that this device details have changed.
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] BEGIN IF");
            // Respond to this device's wifi state changing

            Log.d(TAG, "[WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] Something happened 2 here");
            Log.d(TAG, "[WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] getExtras(): " + intent.getExtras().keySet());
            //NOT needed for our use case
//            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(
//                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//            fragment.updateThisDevice(device);
            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Constants.myMACAddress=device.deviceAddress;

            Log.d(TAG, "Your device information");
            Log.d(TAG, "device Name: " + device.deviceName);
            Log.d(TAG, "device MAC Address: " + device.deviceAddress);
            Log.d(TAG, "deviceStatus: " + device.status);
            Log.d(TAG, "deviceStatus interpretation: CONNECTED = 0, INVITED = 1, FAILED = 2, AVAILABLE = 3, UNAVAILABLE = 4");
            Log.d(TAG, "device primary device type: " + device.primaryDeviceType);
            Log.d(TAG, "device secondary device type: " + device.secondaryDeviceType);

            Log.d(TAG, "WiFiDirectBroadcastReceiver [WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] END IF");
            /*if (!haveSeenThisDeviceChangedEvent) {
                activity.deviceName = device.deviceName;
                activity.changeDeviceName(0);
                haveSeenThisDeviceChangedEvent=true;
            }*/
        }
        Log.d(TAG, "WiFiDirectBroadcastReceiver.onReceive() END");
    }
}
