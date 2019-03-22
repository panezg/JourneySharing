package org.cs7cs3.team7.wifidirect;

public interface ICommsManager {
    enum MESSAGE_TYPES {JOURNEY_MATCH_REQUEST, MATCHING_RESULT}

    ;

    void requestJourneyMatch(UserInfo userInfo);

    String getMACAddress();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
