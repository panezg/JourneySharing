package org.cs7cs3.team7.wifidirect;

import org.cs7cs3.team7.journeysharing.Models.MatchingResultInfo;
import org.cs7cs3.team7.journeysharing.Models.ScheduledJourneyInfo;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;

import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

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
    private String destinationIP;
    private String originIP;
    private ICommsManager.MESSAGE_TYPES messageType;
    private Object payload;
    private String payloadClass;

    public Message(String destinationIP, ICommsManager.MESSAGE_TYPES messageType) {
        this.destinationIP = destinationIP;
        this.messageType = messageType;
        if (messageType == ICommsManager.MESSAGE_TYPES.JOURNEY_MATCH_REQUEST) {
            this.payloadClass = UserInfo.class.toString();
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

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public void setOriginIP(String originIP) {
        this.originIP = originIP;
    }

    public String getOriginIP() {
        return originIP;
    }

    public ICommsManager.MESSAGE_TYPES getMessageType() {
        return messageType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

    public static Message fromJSON(String JSON) {
        Message message = new Gson().fromJson(JSON, Message.class);
        if (message.messageType == ICommsManager.MESSAGE_TYPES.JOURNEY_MATCH_REQUEST) {
            String tempJSON = new Gson().toJson(message.getPayload(), LinkedTreeMap.class);
            UserInfo userInfo = new Gson().fromJson(tempJSON, UserInfo.class);
            message.setPayload(userInfo);
        }
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "destinationIP='" + destinationIP + '\'' +
                ", originIP='" + originIP + '\'' +
                ", messageType=" + messageType +
                ", payload=" + payload +
                ", payloadClass='" + payloadClass + '\'' +
                '}';
    }
}
