package org.cs7cs3.team7.wifidirect;

import android.content.Context;

public class NetworkManagerFactory {
    private static INetworkManager inm;
    private static boolean isSimulatorModeOn = false;

    /*
    public static INetworkManager getNetworkManager(Context context, P2PCommsManager commsManager) {
        if (inm == null) {
            if (isSimulatorModeOn) {
                inm = new DummyNetworkManager();
            }
            else {
                inm = new NetworkManager(context, commsManager);
            }
        }
        return inm;
    }

    public static void setSimulatorModeOn(boolean value) {
        isSimulatorModeOn = value;
    }
    */
}
