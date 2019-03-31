package org.cs7cs3.team7.wifidirect;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;

public class DummyCommsManager implements ICommsManager {
    @Override
    public void requestJourneyMatch(JourneyRequest userInfo) {
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
