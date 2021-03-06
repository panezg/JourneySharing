package org.cs7cs3.team7.journeysharing;

public class Constants {
    public static int LISTENING_PORT = 8889;
    //public static int SENDING_PORT=8889;

    public static int REQUEST_TIMEOUT = 8888;

    public static String THIS_DEVICE_MAC_ADDRESS;

    public static int NETWORK_MANAGER_MONITOR_WAITING_PERIOD = 15000;//millisecond

    public static int THRESHOLD_TO_START_ROUTING = 19;

    public static String HOST = "http://35.196.96.75/";

    public static final String JOURNEY_MATCH_RESULT_INTENT_ACTION = "JOURNEY_MATCH_RESULT";
    public static final String JOURNEY_MATCH_RESULT_INTENT_ACTION_PARCELABLE_KEY = "matchingResult";
}
