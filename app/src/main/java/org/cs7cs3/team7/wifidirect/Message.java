package org.cs7cs3.team7.wifidirect;

import java.util.ArrayList;

public class Message {
    private String fromMAC;
    private String fromIP;
    private String timeStamp;
    private String messageText;

    // User Info
    private UserInfo sender;
    private ArrayList<UserInfo> list;

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public ArrayList<UserInfo> getList() {
        return list;
    }

    public void setList(ArrayList<UserInfo> list) {
        this.list = list;
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

    public String toString() {
        return "Message{" +
                "fromMAC='" + fromMAC + '\'' +
                ", fromIP='" + fromIP + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", messageText='" + messageText + '\'' +
                "sender='" + sender.toString() + '\'' +
                "list='" + list.toString() + '\'' +
                '}';
    }
}