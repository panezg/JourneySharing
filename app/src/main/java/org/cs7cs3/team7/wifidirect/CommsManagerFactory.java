package org.cs7cs3.team7.wifidirect;

import android.content.Context;

public class CommsManagerFactory {
    private static ICommsManager icm;
    private static boolean isSimulatorModeOn = false;

    public static ICommsManager getCommsManager(Context context, String userLoginOnLocal) {
        if (icm == null) {
            if (isSimulatorModeOn) {
                icm = new DummyCommsManager();
            } else {
                icm = new P2PCommsManager(context, userLoginOnLocal);
            }
        }
        return icm;
    }

    public static void setSimulatorModeOn(boolean value) {
        isSimulatorModeOn = value;
    }
}

