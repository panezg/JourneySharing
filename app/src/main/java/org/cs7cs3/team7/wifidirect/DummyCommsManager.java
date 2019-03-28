package org.cs7cs3.team7.wifidirect;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;

public class DummyCommsManager implements ICommsManager {
    @Override
    public void requestJourneyMatch(JourneyRequestInfo userInfo) {
    }

    @Override
    public String getMACAddress() {
        return "";
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
