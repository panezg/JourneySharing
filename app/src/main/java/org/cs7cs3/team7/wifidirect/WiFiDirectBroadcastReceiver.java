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
 * A BroadcastReceiver that is alerted of important WiFi P2P events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private static final String WIFI_P2P_DEBUG_LABEL = "JINCHI";

    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private NetworkManager networkManager;

    private boolean hasWiFiOffOnSequenceBeenDetectedAtLeastOnce = false;
    private String eventChain = "";

    /**
     * @param wifiP2pManager WifiP2pManager system service
     * @param channel        Wifi p2p channel
     * @param networkManager networkManager associated with the receiver
     */
    public WiFiDirectBroadcastReceiver(WifiP2pManager wifiP2pManager, Channel channel,
                                       NetworkManager networkManager) {
        super();
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.networkManager = networkManager;
    }

    /*
     * (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver.onReceive() BEGIN");
        String action = intent.getAction();

        //WIFI_P2P_STATE_CHANGED_ACTION broadcast is received when Wi-Fi P2P is enabled or disabled on the device.
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_STATE_CHANGED_ACTION] BEGIN IF");

            // Check to see if Wi-Fi is enabled and notify appropriate networkManager
            //Check if WiFi P2P/direct is on or off
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct mode is enabled
                Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_STATE_CHANGED_ACTION] WiFi P2P is on");
                networkManager.setIsWifiP2pEnabled(true);
                this.eventChain = this.eventChain.concat("ON");
            } else {
                Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_STATE_CHANGED_ACTION] WiFi P2P is off");
                networkManager.setIsWifiP2pEnabled(false);
                this.eventChain = this.eventChain.concat("OFF");
            }

            Log.d(WIFI_P2P_DEBUG_LABEL, "Event chain: " + eventChain);
            if (eventChain.endsWith("OFFON")) {
                hasWiFiOffOnSequenceBeenDetectedAtLeastOnce = true;
            }
            else {

            }
            Log.d(WIFI_P2P_DEBUG_LABEL, "P2P state changed to: " + state + ". 1 means Disabled, 2 means Enabled");
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_STATE_CHANGED_ACTION] END IF");
        }
        //WIFI_P2P_PEERS_CHANGED_ACTION broadcast is received after calling discoverPeers() and if peers are available nearby
        //You usually want to call requestPeers() to get an updated list of peers if you handle this intent in your application
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_PEERS_CHANGED_ACTION] BEGIN IF");
            //At this point, the WiFi P2P framework only tells you the list of peers has changed, either because existing peers have changed properties
            //or some peers disappeared or new peers appeared.
            Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_PEERS_CHANGED_ACTION] P2P peers changed");
            Log.d(WIFI_P2P_DEBUG_LABEL, "Is wifiP2pManager null? " + (wifiP2pManager == null));
            if (wifiP2pManager != null) {
                Log.d(WIFI_P2P_DEBUG_LABEL, "hasWiFiOffOnSequenceBeenDetectedAtLeastOnce? " + hasWiFiOffOnSequenceBeenDetectedAtLeastOnce);
                //Only process the relevant events of this kind, that is, the events produced after
                //the on-off reset of the WiFi, which happens before the application subscribes to
                //the WiFiP2pManager events
                if (hasWiFiOffOnSequenceBeenDetectedAtLeastOnce) {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WifiP2pManager.requestPeers() from BroadcastReceiver");
                    //To actually get the list of peers and the details of each peer, call WiFiP2pManager.requestPeers(Context, PeerListListener). This is an async call.
                    //The onPeersAvailable() method of PeerListListener will be called once the information is available
                    //NetworkManager implements the PeerListListener interface
                    wifiP2pManager.requestPeers(channel, networkManager);
                }
            }
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_PEERS_CHANGED_ACTION] END IF");
        }
        //WIFI_P2P_CONNECTION_CHANGED_ACTION broadcast is received when the connection state from the PoV of this device has changed
        //usually because either this devices successfully connected to a peer, created a group on its own, or joined an existing group
        //In the case when this device attempts to connect to another one, this event is raised when the second devices accepts the connection request
        //The event comes with a list for each device it has connection change information, e.g., if 3 devices are connected in total in a group,
        //each device will get one WIFI_P2P_CONNECTION_CHANGED_ACTION event but with a list of two entries
        //Broadcast received when the state of the device's Wi-Fi connection changes.
        //Broadcast intent action indicating that the state of Wi-Fi p2p connectivity has changed.
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_CONNECTION_CHANGED_ACTION] BEGIN IF");
            Log.d(WIFI_P2P_DEBUG_LABEL, "Is wifiP2pManager null? " + (wifiP2pManager == null));
            if (wifiP2pManager != null) {
                //WifiP2pInfo provides P2P connection information about a Wi-Fi P2P group
                WifiP2pInfo p2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] p2pInfo (represents connection information about a Wi-Fi P2p group): " + p2pInfo.toString());
                //NetworkInfo provides the status of the network interface
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] networkInfo (describes the status of the network interface): " + networkInfo.toString());
                //A WifiP2pGroup consists of a single group owner and one or more clients.
                //WifiP2pGroup provides information about the group per se, such as the members and their MACs
                WifiP2pGroup wifiP2pGroup = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
                Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] wifiP2pGroup (represents a Wi-Fi P2p group): " + wifiP2pGroup.toString());

                //If this device is part of a group
                if (networkInfo.isConnected()) {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] This device is connected/part of a group");
                    //Only process the relevant events of this kind, that is, the events produced after
                    //the on-off reset of the WiFi, which happens before the application subscribes to
                    //the WiFiP2pManager events
                    //Whether a WiFi Off event has been detected before is only relevant if we plan to call a method of WiFiP2pManager
                    Log.d(WIFI_P2P_DEBUG_LABEL, "hasWiFiOffOnSequenceBeenDetectedAtLeastOnce? " + hasWiFiOffOnSequenceBeenDetectedAtLeastOnce);
                    if (hasWiFiOffOnSequenceBeenDetectedAtLeastOnce) {
                        Log.d(WIFI_P2P_DEBUG_LABEL, "Calling WifiP2pManager.requestConnectionInfo() from BroadcastReceiver");
                        //At this point the MAC addresses of members of the group is available, but to get the IP address of the group owner,
                        //call WiFiP2pManager.requestConnectionInfo(Context, ConnectionInfoListener). This is an async call.
                        //The onConnectionInfoAvailable() method of ConnectionInfoListener will be called once the information is available
                        //NetworkManager implements the ConnectionInfoListener interface
                        wifiP2pManager.requestConnectionInfo(channel, networkManager);
                    }
                } else {
                    Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_CONNECTION_CHANGED_ACTION] This device is NOT or NOT LONGER connected/part of a group");
                }
            }
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_CONNECTION_CHANGED_ACTION] END IF");
        }
        //WIFI_P2P_THIS_DEVICE_CHANGED_ACTION broadcast is received when this device's details have
        //changed, such as the device's status, e.g., available, connected, etc.
        //Although this event provides information about your current state, such as your own MAC address. It is not that useful
        //since other events provide the same information
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] BEGIN IF");
            Log.d(WIFI_P2P_DEBUG_LABEL, "[WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] getExtras(): " + intent.getExtras().keySet());
            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.d(WIFI_P2P_DEBUG_LABEL, "Your device information");
            Log.d(WIFI_P2P_DEBUG_LABEL, "device Name: " + device.deviceName);
            Log.d(WIFI_P2P_DEBUG_LABEL, "device MAC Address: " + device.deviceAddress);
            Log.d(WIFI_P2P_DEBUG_LABEL, "deviceStatus: " + device.status);
            Log.d(WIFI_P2P_DEBUG_LABEL, "deviceStatus interpretation: CONNECTED = 0, INVITED = 1, FAILED = 2, AVAILABLE = 3, UNAVAILABLE = 4");
            Log.d(WIFI_P2P_DEBUG_LABEL, "device primary device type: " + device.primaryDeviceType);
            Log.d(WIFI_P2P_DEBUG_LABEL, "device secondary device type: " + device.secondaryDeviceType);
            Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver [WIFI_P2P_THIS_DEVICE_CHANGED_ACTION] END IF");

            Constants.THIS_DEVICE_MAC_ADDRESS = device.deviceAddress;
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "WiFiDirectBroadcastReceiver.onReceive() END");
    }
}
