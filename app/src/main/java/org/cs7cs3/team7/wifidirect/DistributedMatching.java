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

        //debug all requests
        for (String key : journeyRequests.keySet()) {
            JourneyRequestInfo journeyRequestInfo = journeyRequests.get(key);
            Log.d(WIFI_P2P_DEBUG_LABEL, "journeyRequestInfo: " + journeyRequestInfo);
        }

        //match by destination
        Map<String, List<JourneyRequestInfo>> destinationGroups = new HashMap<>();
        for (String key : journeyRequests.keySet()) {
            JourneyRequestInfo journeyRequestInfo = journeyRequests.get(key);
            String destination = journeyRequestInfo.getDestination();
            if (destinationGroups.get(destination) == null) {
                destinationGroups.put(destination, new ArrayList<>());
            }
            destinationGroups.get(destination).add(journeyRequestInfo);
        }

        //match by own/preference gender
        Map<String, List<JourneyRequestInfo>> destinationOwnPreferenceGenderGroups = new HashMap<>();
        for (String key : destinationGroups.keySet()) {
            List<JourneyRequestInfo> destinationGroup = destinationGroups.get(key);
            for (JourneyRequestInfo journeyRequestInfo : destinationGroup) {
                String destination = journeyRequestInfo.getDestination();
                String ownGender = journeyRequestInfo.getUserInfo().getGender();
                String preferredGender = journeyRequestInfo.getGender();
                //own: male, pre: female and own: female, pre: male should match
                if (destinationOwnPreferenceGenderGroups.get(destination + "-" + preferredGender + "-" + ownGender) != null) {
                    destinationOwnPreferenceGenderGroups.get(destination + "-" + preferredGender + "-" + ownGender).add(journeyRequestInfo);
                }
                else {
                    if (destinationOwnPreferenceGenderGroups.get(destination + "-" + ownGender + "-" + preferredGender) == null) {
                        destinationOwnPreferenceGenderGroups.put(destination + "-" + ownGender + "-" + preferredGender, new ArrayList<>());
                    }
                    destinationOwnPreferenceGenderGroups.get(destination + "-" + ownGender + "-" + preferredGender).add(journeyRequestInfo);
                }
            }
        }

        //match by method of transportation
        Map<String, List<JourneyRequestInfo>> destinationOwnPreferenceGenderMethodGroups = new HashMap<>();
        for (String key : destinationOwnPreferenceGenderGroups.keySet()) {
            List<JourneyRequestInfo> destinationOwnPreferenceGenderGroup = destinationOwnPreferenceGenderGroups.get(key);
            for (JourneyRequestInfo journeyRequestInfo : destinationOwnPreferenceGenderGroup) {
                String destination = journeyRequestInfo.getDestination();
                String ownGender = journeyRequestInfo.getUserInfo().getGender();
                String preferredGender = journeyRequestInfo.getGender();
                String method = journeyRequestInfo.getMethod();

                if (destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + preferredGender + "-" + ownGender + "-" + method) != null) {
                    destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + preferredGender + "-" + ownGender + "-" + method).add(journeyRequestInfo);
                }
                else {
                    if (destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + ownGender + "-" + preferredGender + "-" + method) == null) {
                        destinationOwnPreferenceGenderMethodGroups.put(destination + "-" + ownGender + "-" + preferredGender + "-" + method, new ArrayList<>());
                    }
                    destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + ownGender + "-" + preferredGender + "-" + method).add(journeyRequestInfo);
                }
            }
        }

        //divide in groups
        List<MatchingResultInfo> matchingResultInfoList = new ArrayList<>();
        for (String key : destinationOwnPreferenceGenderMethodGroups.keySet()) {
            List<JourneyRequestInfo> destinationOwnPreferenceGenderMethodGroup = destinationOwnPreferenceGenderMethodGroups.get(key);
            // If the groups' method of transportation is taxi, we need to divide into multiple groups
            // because of car's limited capacity
            if (destinationOwnPreferenceGenderMethodGroup.get(0).getMethod() == "Taxi") {
                int taxiCapacity = 3;
                int numSubGroups = 0;
                for (int j = taxiCapacity; j <= destinationOwnPreferenceGenderMethodGroup.size(); j += taxiCapacity) {
                    MatchingResultInfo matchingResultInfo = new MatchingResultInfo(new ArrayList<>());
                    for (int k = j - taxiCapacity; k < j; k++) {
                        JourneyRequestInfo journeyRequestInfo = destinationOwnPreferenceGenderMethodGroup.get(k);
                        matchingResultInfo.getGroupMembers().add(journeyRequestInfo.getUserInfo());
                    }
                    matchingResultInfo.setStatus(MatchingResultInfo.MatchingResultStatus.MATCHED);
                    matchingResultInfoList.add(matchingResultInfo);
                    numSubGroups++;
                }
                //Figuring out if remainders could be grouped too
                //There are no groups of 1 member
                if (destinationOwnPreferenceGenderMethodGroup.size() - (numSubGroups * taxiCapacity) > 1) {
                    MatchingResultInfo matchingResultInfo = new MatchingResultInfo(new ArrayList<>());
                    for (int k = numSubGroups * taxiCapacity; k < destinationOwnPreferenceGenderMethodGroup.size(); k++) {
                        JourneyRequestInfo journeyRequestInfo = destinationOwnPreferenceGenderMethodGroup.get(k);
                        matchingResultInfo.getGroupMembers().add(journeyRequestInfo.getUserInfo());
                    }
                    matchingResultInfo.setStatus(MatchingResultInfo.MatchingResultStatus.MATCHED);
                    matchingResultInfoList.add(matchingResultInfo);
                }
                //TODO: Send reject messages to those with groups of one member
            }
            else {
                //TODO: Send reject messages to those with groups of one member
                MatchingResultInfo matchingResultInfo = new MatchingResultInfo(new ArrayList<>());
                for (JourneyRequestInfo journeyRequestInfo : destinationOwnPreferenceGenderMethodGroup) {
                    matchingResultInfo.getGroupMembers().add(journeyRequestInfo.getUserInfo());
                }
                matchingResultInfo.setStatus(MatchingResultInfo.MatchingResultStatus.MATCHED);
                matchingResultInfoList.add(matchingResultInfo);
            }
        }

        Log.d(WIFI_P2P_DEBUG_LABEL, "Matching is done and groups are ready!");
        p2pCommsManager.broadcastMatchingResult(matchingResultInfoList);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END DistributedMatching.match()");
    }

    private void addTestData() {
        UserInfo userInfo = new UserInfo("1-pankaj", "pankaj", "8982300456", "Male");
        JourneyRequestInfo journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("2-shashank", "shashank", "8982210466", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("3-vishal", "vishal", "8982210173", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin castle");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("4-gunjan", "gunjan", "8982210581", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin castle");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        userInfo = new UserInfo("5-swara", "swara", "8982167149", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
        //More
        userInfo = new UserInfo("6-jhon", "john", "111", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("7-rebecca", "rebecca", "222", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("8-paula", "paula", "333", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("9-ringo", "ringo", "444", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("10-sara", "sara", "555", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Taxi", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("11-laura", "laura", "666", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Walking", "dublin city center");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("12-carla", "carla", "777", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("13-peter", "peter", "888", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("14-justin", "justin", "999", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Walking", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("15-frank", "frank", "101", "Male");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Male", "Taxi", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("16-daphne", "daphne", "102", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Walking", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("17-candice", "candice", "103", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);

        userInfo = new UserInfo("18-katrina", "katrina", "104", "Female");
        journeyRequestInfo = new JourneyRequestInfo(userInfo, "Female", "Taxi", "TCD");
        journeyRequests.put(userInfo.getId(), journeyRequestInfo);
    }
}
