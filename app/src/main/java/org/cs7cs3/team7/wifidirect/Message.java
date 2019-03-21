package org.cs7cs3.team7.wifidirect;

import org.cs7cs3.team7.journeysharing.Models.MatchingResultInfo;
import org.cs7cs3.team7.journeysharing.Models.ScheduledJourneyInfo;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;

import java.util.Map;

public class Message {
    private String fromMAC;
    private String fromIP;
    private String timeStamp;
    private String messageText;
    private String intent;

    private UserInfo sender;
    private MatchingResultInfo matchingResultInfo;
    private ScheduledJourneyInfo scheduledJourneyInfo;

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public MatchingResultInfo getMatchingResultInfo() {
        return matchingResultInfo;
    }

    public void setMatchingResultInfo(MatchingResultInfo matchingResultInfo) {
        this.matchingResultInfo = matchingResultInfo;
    }

    public ScheduledJourneyInfo getScheduledJourneyInfo() {
        return scheduledJourneyInfo;
    }

    public void setScheduledJourneyInfo(ScheduledJourneyInfo scheduledJourneyInfo) {
        this.scheduledJourneyInfo = scheduledJourneyInfo;
    }

    public String getFromMAC() {
        return fromMAC;
    }

    public void setFromMAC(String fromMAC) {
        this.fromMAC = fromMAC;
    }

    public String getFromIP() {
        return fromIP;
    }

    public void setFromIP(String fromIP) {
        this.fromIP = fromIP;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "fromMAC='" + fromMAC + '\'' +
                ", fromIP='" + fromIP + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", messageText='" + messageText + '\'' +
                ", intent='" + intent + '\'' +
                '}';
    }
}
