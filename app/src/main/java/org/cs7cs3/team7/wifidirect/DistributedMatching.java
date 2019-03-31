package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.Models.MatchingResult;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributedMatching {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI_P2P_MATCHING";

    private P2PCommsManager p2pCommsManager;

    private Map<String, JourneyRequest> journeyRequests;


    public DistributedMatching(P2PCommsManager p2pCommsManager) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "P2P Matching constructor");
        this.p2pCommsManager = p2pCommsManager;
        journeyRequests = new HashMap<>();
    }

    public void addJourneyRequest(JourneyRequest journeyRequest) {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN DistributedMatching.addJourneyRequest()");
        //as we dont have many devices, let's make some data
        if (journeyRequests.size() == 0) {
            Log.d(WIFI_P2P_DEBUG_LABEL, "Adding test data to simulate people around");
            addTestData();
        }
        //using userInfo, but should be an id-like value instead
        journeyRequests.put(journeyRequest.getUser().getLogin(), journeyRequest);
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
            JourneyRequest journeyRequest = journeyRequests.get(key);
            Log.d(WIFI_P2P_DEBUG_LABEL, "journeyRequest: " + journeyRequest);
        }

        //match by destination
        Map<String, List<JourneyRequest>> destinationGroups = new HashMap<>();
        for (String key : journeyRequests.keySet()) {
            JourneyRequest journeyRequest = journeyRequests.get(key);
            String destination = journeyRequest.getDestination();
            if (destinationGroups.get(destination) == null) {
                destinationGroups.put(destination, new ArrayList<>());
            }
            destinationGroups.get(destination).add(journeyRequest);
        }

        //match by own/preference gender
        Map<String, List<JourneyRequest>> destinationOwnPreferenceGenderGroups = new HashMap<>();
        for (String key : destinationGroups.keySet()) {
            List<JourneyRequest> destinationGroup = destinationGroups.get(key);
            for (JourneyRequest journeyRequest : destinationGroup) {
                String destination = journeyRequest.getDestination();
                String ownGender = journeyRequest.getUser().getGender();
                String preferredGender = journeyRequest.getGender();
                //own: male, pre: female and own: female, pre: male should match
                if (destinationOwnPreferenceGenderGroups.get(destination + "-" + preferredGender + "-" + ownGender) != null) {
                    destinationOwnPreferenceGenderGroups.get(destination + "-" + preferredGender + "-" + ownGender).add(journeyRequest);
                }
                else {
                    if (destinationOwnPreferenceGenderGroups.get(destination + "-" + ownGender + "-" + preferredGender) == null) {
                        destinationOwnPreferenceGenderGroups.put(destination + "-" + ownGender + "-" + preferredGender, new ArrayList<>());
                    }
                    destinationOwnPreferenceGenderGroups.get(destination + "-" + ownGender + "-" + preferredGender).add(journeyRequest);
                }
            }
        }

        //match by method of transportation
        Map<String, List<JourneyRequest>> destinationOwnPreferenceGenderMethodGroups = new HashMap<>();
        for (String key : destinationOwnPreferenceGenderGroups.keySet()) {
            List<JourneyRequest> destinationOwnPreferenceGenderGroup = destinationOwnPreferenceGenderGroups.get(key);
            for (JourneyRequest journeyRequest : destinationOwnPreferenceGenderGroup) {
                String destination = journeyRequest.getDestination();
                String ownGender = journeyRequest.getUser().getGender();
                String preferredGender = journeyRequest.getGender();
                String method = journeyRequest.getMethod();

                if (destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + preferredGender + "-" + ownGender + "-" + method) != null) {
                    destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + preferredGender + "-" + ownGender + "-" + method).add(journeyRequest);
                }
                else {
                    if (destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + ownGender + "-" + preferredGender + "-" + method) == null) {
                        destinationOwnPreferenceGenderMethodGroups.put(destination + "-" + ownGender + "-" + preferredGender + "-" + method, new ArrayList<>());
                    }
                    destinationOwnPreferenceGenderMethodGroups.get(destination + "-" + ownGender + "-" + preferredGender + "-" + method).add(journeyRequest);
                }
            }
        }

        //divide in groups
        List<MatchingResult> matchingResultList = new ArrayList<>();

        for (String key : destinationOwnPreferenceGenderMethodGroups.keySet()) {
            List<JourneyRequest> destinationOwnPreferenceGenderMethodGroup = destinationOwnPreferenceGenderMethodGroups.get(key);
            // If the groups' method of transportation is taxi, we need to divide into multiple groups
            // because of car's limited capacity
            if (destinationOwnPreferenceGenderMethodGroup.get(0).getMethod() == "Taxi") {
                int taxiCapacity = 3;

                for (int j = 0; j < destinationOwnPreferenceGenderMethodGroup.size(); j += taxiCapacity) {
                    int indexLimit = Math.min(j + taxiCapacity, destinationOwnPreferenceGenderMethodGroup.size());
                //for (int j = taxiCapacity; j <= destinationOwnPreferenceGenderMethodGroup.size(); j += taxiCapacity) {
                    MatchingResult matchingResult = new MatchingResult(new ArrayList<>());
                    //for (int k = j - taxiCapacity; k < j; k++) {
                    for (int k = j; k < indexLimit; k++) {
                        JourneyRequest journeyRequest = destinationOwnPreferenceGenderMethodGroup.get(k);
                        matchingResult.getGroupMembers().add(journeyRequest.getUser());
                    }
                    //The matching algorithm will return groups of 1 to those users who didn't have a match
                    //The user in that group would be the one that requested the match
                    //A matchingResult with a list of group members of 1 is therefore not a match
                    if (matchingResult.getGroupMembers().size() > 1) {
                        matchingResult.setStatus(MatchingResult.MatchingResultStatus.MATCHED);
                    } else {
                        matchingResult.setStatus(MatchingResult.MatchingResultStatus.NO_MATCH);
                    }
                    matchingResultList.add(matchingResult);
                }
            }
            else {
                MatchingResult matchingResult = new MatchingResult(new ArrayList<>());
                for (JourneyRequest journeyRequest : destinationOwnPreferenceGenderMethodGroup) {
                    matchingResult.getGroupMembers().add(journeyRequest.getUser());
                }
                //The matching algorithm will return groups of 1 to those users who didn't have a match
                //The user in that group would be the one that requested the match
                //A matchingResult with a list of group members of 1 is therefore not a match
                if (matchingResult.getGroupMembers().size() > 1) {
                    matchingResult.setStatus(MatchingResult.MatchingResultStatus.MATCHED);
                } else {
                    matchingResult.setStatus(MatchingResult.MatchingResultStatus.NO_MATCH);
                }
                matchingResultList.add(matchingResult);
            }
        }

        Log.d(WIFI_P2P_DEBUG_LABEL, "Matching is done and groups are ready!");
        p2pCommsManager.broadcastMatchingResult(matchingResultList);
        Log.d(WIFI_P2P_DEBUG_LABEL, "END DistributedMatching.match()");
    }

    private void addTestData() {
        User user = new User(1,"1-pankaj", "pankaj", "8982300456", "Male");
        JourneyRequest journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(2,"2-shashank", "shashank", "8982210466", "Male");
        journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(3,"3-vishal", "vishal", "8982210173", "Male");
        journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin castle", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(4,"4-gunjan", "gunjan", "8982210581", "Male");
        journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin castle", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(5, "5-swara", "swara", "8982167149", "Male");
        journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(6,"6-jhon", "john", "111", "Male");
        journeyRequest = new JourneyRequest(user, "Female", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(7,"7-rebecca", "rebecca", "222", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(8,"8-paula", "paula", "333", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(9,"9-ringo", "ringo", "444", "Male");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(10,"10-sara", "sara", "555", "Female");
        journeyRequest = new JourneyRequest(user, "Male", "Taxi", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(11,"11-laura", "laura", "666", "Female");
        journeyRequest = new JourneyRequest(user, "Male", "Walking", "dublin city center", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(12,"12-carla", "carla", "777", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(13,"13-peter", "peter", "888", "Male");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(14,"14-justin", "justin", "999", "Male");
        journeyRequest = new JourneyRequest(user, "Female", "Walking", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(15,"15-frank", "frank", "101", "Male");
        journeyRequest = new JourneyRequest(user, "Male", "Taxi", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(16,"16-daphne", "daphne", "102", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Walking", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(17,"17-candice", "candice", "103", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);

        user = new User(18,"18-katrina", "katrina", "104", "Female");
        journeyRequest = new JourneyRequest(user, "Female", "Taxi", "TCD", true);
        journeyRequests.put(user.getLogin(), journeyRequest);
    }
}
