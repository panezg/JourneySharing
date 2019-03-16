package org.cs7cs3.team7.wifidirect;

public class UserInfo {
    // User Info
    private String name;
    private String phoneNum;
    // TODO: Need to wait @JInchi finished the new Fragment, and then just read the @param destination info from the mainViewModel.
    private final String destination = "fakeDestination";

    public UserInfo(String name, String phone, String des) {
        this.name = name;
        phoneNum = phone;
        //destination = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        // TODO
        //this.destination = destination;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }

}