package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;
import org.cs7cs3.team7.journeysharing.Models.MatchingResultInfo;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributedMatching {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI_P2P_MATCHING";

    private P2PCommsManager p2pCommsManager;

    private Map<String, JourneyRequestInfo> journeyRequests;


    public DistributedMatching(P2PCommsManager p2pCommsManager) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "P2P Matching constructor");
        this.p2pCommsManager = p2pCommsManager;
        journeyRequests = new HashMap<>();
    }

    public void addJourneyRequest(JourneyRequestInfo journeyRequestInfo) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN DistributedMatching.addJourneyRequest()");
        //as we dont have many devices, let's make some data
        if (journeyRequests.size() == 0) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Adding test data to simulate people around");
            addTestData();
        }
        //using userInfo, but should be an id-like value instead
        journeyRequests.put(journeyRequestInfo.getUserInfo().getId(), journeyRequestInfo);
        if (journeyRequests.size() > Constants.THRESHOLD_TO_START_ROUTING) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Enough people to run matching logic. Calling match()");
            this.match();
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "No. Peers' request recorded " + journeyRequests.size());
        Log.d(WIFI_P2P_DEBUG_LABEL, "END DistributedMatching.addJourneyRequest()");
    }

    private void match() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN DistributedMatching.match()");
        int i = 0;

        List<MatchingResultInfo> matchingResultInfoList = new ArrayList<>();
        MatchingResultInfo matchingResultInfo = new MatchingResultInfo(new ArrayList<>());
        matchingResultInfo.setStatus(MatchingResultInfo.MatchingResultStatus.MATCHED);
        matchingResultInfoList.add(matchingResultInfo);
        matchingResultInfo = new MatchingResultInfo(new ArrayList<>());
        matchingResultInfo.setStatus(MatchingResultInfo.MatchingResultStatus.MATCHED);
        matchingResultInfoList.add(matchingResultInfo);
        for (String key : journeyRequests.keySet()) {
            JourneyRequestInfo journeyRequestInfo = journeyRequests.get(key);
            matchingResultInfoList.get(i++ % 2).getGroupMembers().add(journeyRequestInfo.getUserInfo());
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "Matching is done and groups are ready!");
        p2pCommsManager.broadcastMatchingResult(matchingResultInfoList);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END DistributedMatching.match()");
    }

    private void addTestData() {
        UserInfo userInfo = new UserInfo("1-pankaj", "pankaj", "8982300456", "dublin city center");
        JourneyRequestInfo journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("2-shashank", "shashank", "8982210466", "dublin city center");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("3-vishal", "vishal", "8982210173", "dublin castle");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("4-gunjan", "gunjan", "8982210581", "dublin castle");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("5-swara", "swara", "8982167149", "dublin city center");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
    }
}
