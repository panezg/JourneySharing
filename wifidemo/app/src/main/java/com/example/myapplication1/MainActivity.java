package com.example.myapplication1;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    Button btn;
    EditText userName;
    EditText msg;
    TextView textView;
    NetworkManager networkManager;

    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifip2pChannel;
    WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
    private boolean isWDConnected = false;

    private List<DeviceDTO> deviceList;
    private HashSet<String> deviceAddressList;

    private Context context;
    private boolean isWifiP2pEnabled = false;
    public boolean isWifiP2pEnabled() {
        return isWifiP2pEnabled;
    }
    public void setIsWifiP2pEnabled(boolean wifiP2pEnabled) {
        isWifiP2pEnabled = wifiP2pEnabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        userName = findViewById(R.id.userNameTextView);

        msg = findViewById(R.id.msgTextView);
        textView = (TextView) findViewById(R.id.textView);
        //networkManager = new NetworkManager(getApplicationContext());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "user:" + userName.getText().toString() + "msg: " + msg.getText().toString();
                textView.setText(str);
                send(userName.getText().toString(), msg.getText().toString());

            }
        });

        context=getApplicationContext();
        initialize();
    }

    void send(String user, String msg) {
        findPeers();
    }
    private void initialize() {
        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        wifiManager.setWifiEnabled(true);
        wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        wifip2pChannel = wifiP2pManager.initialize(this, getMainLooper(), null);


        //wiFiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager,
          //      wifip2pChannel, this);
        //registerReceiver(wiFiDirectBroadcastReceiver, wifip2pFilter);

    }

    public void findPeers() {
        Log.d("JINCHI", "onside find peers");
        if (!isWDConnected) {
            Log.d("JINCHI", "Trying peer finding");
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

    @Override
    public void onPeersAvailable(WifiP2pDeviceList list) {
        if(list.getDeviceList().size()==0){
            Log.d("JINCHI", "peer list 0");
            return;
        }
        Log.d("JINCHI", "Size : "+list.getDeviceList().size());

        ArrayList<WifiP2pDevice> peerList= new ArrayList<WifiP2pDevice> (list.getDeviceList());
        List<DeviceDTO> localDeviceList=new ArrayList<>();


        for(WifiP2pDevice peer:peerList){
            DeviceDTO device=new DeviceDTO();
            device.setDeviceAddress(peer.deviceAddress);
            device.setDeviceName(peer.deviceName);

            localDeviceList.add(device);
        }

        List<DeviceDTO> newDevices=alignDevices(localDeviceList);

        for(DeviceDTO deviceDTO:newDevices){
            Log.d("JINCHI", "Connecting to "+deviceDTO.getDeviceAddress()+" "+deviceDTO.getDeviceName());
            Toast.makeText(getApplicationContext(), "Connecting to "+deviceDTO.getDeviceAddress()+" "+deviceDTO.getDeviceName(),Toast.LENGTH_SHORT).show();
            connect(deviceDTO);
        }

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d("JINCHI", "in connection info");
        Gson gson = new GsonBuilder().create();
        if(info.isGroupOwner){
            Toast.makeText(getApplicationContext(), "m owner",Toast.LENGTH_SHORT).show();
            //  info.
            new FileServerAsyncTask(this).execute(getApplicationContext());
        }
        else{
            Toast.makeText(getApplicationContext(), "m client",Toast.LENGTH_SHORT).show();
            Message msg = new Message();
            msg.ip="1.1.1.1";
            msg.port=8888;
            msg.msg=Utility.getWiFiIPAddress(this.getApplicationContext());
            String willSend = gson.toJson(msg, Message.class);
            Log.d("JINCHI", "will send "+willSend);
            new FileClientAsyncTask().execute(info.groupOwnerAddress.getHostAddress(), willSend);

        }
    }

    public void toast(String str){
        Toast.makeText(this.context,str,Toast.LENGTH_SHORT).show();
    }

    List<DeviceDTO> alignDevices(List<DeviceDTO> localDeviceList){
        List<DeviceDTO> newDevices=new ArrayList<>();
        if(deviceList==null){
            deviceList=new ArrayList<>();
        }
        for(int i=0; i<localDeviceList.size();i++){
            boolean flag=true;
            for(int k=0;  k<deviceList.size();k++){
                if(deviceList.get(k).getDeviceAddress().equals(localDeviceList.get(i).getDeviceAddress())){
                    flag=false;
                    break;
                }
            }
            if(flag){
                Log.d("JINCHI", "adding device to list "+localDeviceList.get(i).getDeviceName());
                deviceList.add(localDeviceList.get(i));
                newDevices.add(localDeviceList.get(i));
            }
        }
        return newDevices;
    }

    public void connect(DeviceDTO deviceDTO) {
        //if (!isWDConnected) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = deviceDTO.getDeviceAddress();
            config.wps.setup = WpsInfo.PBC;
            config.groupOwnerIntent = 4;
            wifiP2pManager.connect(wifip2pChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int reasonCode) {
                    Toast.makeText(getApplicationContext(), "Connectiion failed "+reasonCode,Toast.LENGTH_SHORT).show();
                }
            });
        //}
    }

        @Override
    protected void onResume() {
            Log.d("JINCHI", "in on resume");
        super.onResume();
            IntentFilter  wifip2pFilter = new IntentFilter();
            wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
            wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
            wifip2pFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        wiFiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager,
                wifip2pChannel, this);
            Log.d("JINCHI", "filter registered");
        registerReceiver(wiFiDirectBroadcastReceiver, wifip2pFilter);

    }

    @Override
    protected void onDestroy() {
        wifiP2pManager.removeGroup(wifip2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() { }
            @Override
            public void onFailure(int i) { }
        });
        super.onDestroy();
    }

        @Override
    protected void onPause() {
        unregisterReceiver(wiFiDirectBroadcastReceiver);
        super.onPause();
    }

}