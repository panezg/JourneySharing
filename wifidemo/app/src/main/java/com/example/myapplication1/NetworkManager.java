package com.example.myapplication1;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_P2P_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class NetworkManager implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    static int count = 0;
    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifip2pChannel;
    WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    private boolean isWDConnected = false;
    private Context context;
    private boolean isWifiP2pEnabled = false;
    public boolean isWifiP2pEnabled() {
        return isWifiP2pEnabled;
    }
    public void setIsWifiP2pEnabled(boolean wifiP2pEnabled) {
        isWifiP2pEnabled = wifiP2pEnabled;
    }

    public NetworkManager(Context context) {
        this.context = context;
        initialize();

    }

    void send(String user, String msg) {
        findPeers();
    }
    private void initialize() {
/*

//        String myIP = Utility.getWiFiIPAddress(LocalDashWiFiDirect.this);
//        Utility.saveString(LocalDashWiFiDirect.this, TransferConstants.KEY_MY_IP, myIP);


        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(WIFI_P2P_SERVICE);
        wifip2pChannel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        IntentFilter wifip2pFilter = new IntentFilter();
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wiFiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager,
                wifip2pChannel, this);
        context.registerReceiver(wiFiDirectBroadcastReceiver, wifip2pFilter);
*/

    }


    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        Log.d("JINCHI", "Size : "+peerList.getDeviceList().size());
//        if (peerList.getDeviceList().size() == 0) {
//            Log.d("paras", "no devices found");
//            return;
//        }
//
//        Log.d("paras", "Devices found");
//        ArrayList<> deviceDTOs = new ArrayList<>();
//
//        List<WifiP2pDevice> devices = (new ArrayList<>());
//        devices.addAll(peerList.getDeviceList());
//        Log.d("paras", "peer size " + devices.size());
//        // if(devices.size()!=0){
//        for (WifiP2pDevice device : devices) {
//            Log.d("paras", "found " + device.deviceAddress);
//            DeviceDTO deviceDTO = new DeviceDTO();
//            deviceDTO.setIp(device.deviceAddress);
//            Log.d("paras", device.deviceAddress + " " + device.deviceName);
//            deviceDTO.setPlayerName(device.deviceName);
//            deviceDTO.setDeviceName(new String());
//            deviceDTO.setOsVersion(new String());
//            deviceDTO.setPort(-1);
//            deviceDTOs.add(deviceDTO);
//        }

    }



    public void findPeers() {

        if (!isWDConnected) {

            wifiP2pManager.discoverPeers(wifip2pChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int reasonCode) {

                }
            });
        }
    }
}