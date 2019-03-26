package org.cs7cs3.team7.wifidirect;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;
import org.cs7cs3.team7.journeysharing.Models.MatchingResultInfo;

public class Message {
    //TODO:  timeStamp could be useful
    //private String timeStamp;

    private String destinationIP;
    private String originIP;
    private ICommsManager.MESSAGE_TYPES messageType;
    private Object payload;

    public Message(String destinationIP, ICommsManager.MESSAGE_TYPES messageType) {
        this.destinationIP = destinationIP;
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "destinationIP='" + destinationIP + '\'' +
                ", originIP='" + originIP + '\'' +
                ", messageType=" + messageType +
                ", payload=" + payload +
                '}';
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
            JourneyRequestInfo journeyRequestInfo = new Gson().fromJson(tempJSON, JourneyRequestInfo.class);
            message.setPayload(journeyRequestInfo);
        } else if (message.messageType == ICommsManager.MESSAGE_TYPES.MATCHING_RESULT) {
            String tempJSON = new Gson().toJson(message.getPayload(), LinkedTreeMap.class);
            MatchingResultInfo matchingResultInfo = new Gson().fromJson(tempJSON, MatchingResultInfo.class);
            message.setPayload(matchingResultInfo);
        }
        return message;
    }
}
