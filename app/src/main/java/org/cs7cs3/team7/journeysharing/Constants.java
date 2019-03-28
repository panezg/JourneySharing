package org.cs7cs3.team7.journeysharing;

public class Constants {
    public static int LISTENING_PORT = 8889;
    //public static int SENDING_PORT=8889;

    public static int REQUEST_TIMEOUT = 8888;

    public static String THIS_DEVICE_MAC_ADDRESS;

    public static int NETWORK_MANAGER_MONITOR_WAITING_PERIOD = 15000;//millisecond

    public static int THRESHOLD_TO_START_ROUTING = 19;

    public static String HOST = "http://34.73.229.48:8080/JSBackend-1.0/";

    public static final String JOURNEY_MATCHED_INTENT_ACTION = "JOURNEY_MATCHED";
    public static final String JOURNEY_MATCHED_INTENT_ACTION_PARCELABLE_KEY = "matchingResultInfo";
}
