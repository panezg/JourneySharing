package org.cs7cs3.team7.wifidirect;

public interface INetworkManager {
    void initiateWiFiP2PGroupFormation();
    boolean isThisDevicePartOfGroup();
    boolean isThisDeviceGroupOwner();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
