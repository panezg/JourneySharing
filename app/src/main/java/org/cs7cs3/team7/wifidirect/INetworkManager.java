package org.cs7cs3.team7.wifidirect;

public interface INetworkManager {
    void sendMessage(Message message);
    void initiateNetworkActivity();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
