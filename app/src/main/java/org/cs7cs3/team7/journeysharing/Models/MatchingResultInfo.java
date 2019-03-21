package org.cs7cs3.team7.journeysharing.Models;

import java.util.HashMap;

public class MatchingResultInfo {

    // General fields
    HashMap<String, UserInfo> groupMembers;

    // Fields for P2P
    //private int groupID;

    // Fields for online mode
    private String meetingDate;
    private String meetingTime;
    private String meetingPlace;

    public HashMap<String, UserInfo> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(HashMap<String, UserInfo> groupMembers) {
        this.groupMembers = groupMembers;
    }
/*
    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
    */

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }
}
