package org.cs7cs3.team7.wifidirect;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;

public interface ICommsManager {
    enum MESSAGE_TYPES {JOURNEY_MATCH_REQUEST, MATCHING_RESULT}

    ;

    void requestJourneyMatch(JourneyRequestInfo journeyRequestInfo);

    String getMACAddress();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
