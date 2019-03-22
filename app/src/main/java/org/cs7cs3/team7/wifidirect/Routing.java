package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;
import org.cs7cs3.team7.journeysharing.Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

public class Routing {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI_P2P_MATCHING";

    private P2PCommsManager p2pCommsManager;

    private Map<String, UserInfo> journeyRequests;
    private boolean isMatchDone;


    public Routing(P2PCommsManager p2pCommsManager) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "P2P Matching constructor");
        this.p2pCommsManager = p2pCommsManager;
        journeyRequests = new HashMap<>();
        isMatchDone = false;
    }

    public void addJourneyRequest(UserInfo userInfo) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN Routing.addJourneyRequest()");
        //as we dont have many devices, let's make some data
        if (journeyRequests.size() == 0) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Adding test data to simulate people around");
            addTestData();
        }
        //using userInfo, but should be an id-like value instead
        journeyRequests.put(userInfo.getId(), userInfo);
        if (journeyRequests.size() > Constants.THRESHOLD_TO_START_ROUTING) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Enough people to run matching logic. Calling match()");
            this.match();
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "No. Peers' request recorded " + journeyRequests.size());
        Log.d(WIFI_P2P_DEBUG_LABEL, "END Routing.addJourneyRequest()");
    }

    private void match() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN Routing.match()");
        int i = 0;
        List<List<UserInfo>> groups = new ArrayList<List<UserInfo>>();
        groups.add(new ArrayList<UserInfo>());
        groups.add(new ArrayList<UserInfo>());
        for (String key : journeyRequests.keySet()) {
            UserInfo userInfo = journeyRequests.get(key);
            groups.get(i++ % 2).add(userInfo);
        }
        isMatchDone = true;
        Log.d(WIFI_P2P_DEBUG_LABEL, "Matching is done and groups are ready!");
        p2pCommsManager.broadcastMatchingResult(groups);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END Routing.match()");
    }

    private void addTestData() {
        UserInfo userInfo = new UserInfo("1-pankaj", "pankaj", "8982300456", "dublin city center");
        journeyRequests.put(userInfo.getId(), userInfo);
        userInfo = new UserInfo("2-shashank", "shashank", "8982210466", "dublin city center");
        journeyRequests.put(userInfo.getId(), userInfo);
        userInfo = new UserInfo("3-vishal", "vishal", "8982210173", "dublin castle");
        journeyRequests.put(userInfo.getId(), userInfo);
        userInfo = new UserInfo("4-gunjan", "gunjan", "8982210581", "dublin castle");
        journeyRequests.put(userInfo.getId(), userInfo);
        userInfo = new UserInfo("5-swara", "swara", "8982167149", "dublin city center");
        journeyRequests.put(userInfo.getId(), userInfo);
    }

    public boolean isMatchDone() {
        return isMatchDone;
    }

    public Map<String, UserInfo> getJourneyRequests() {
        return journeyRequests;
    }
}
