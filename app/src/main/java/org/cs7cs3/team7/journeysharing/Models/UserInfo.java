package org.cs7cs3.team7.journeysharing.Models;

public class UserInfo {
    // User Info
    private String id;
    private String name;
    private String phoneNum;
    private String gender;

    /* Need to remove */
    // TODO: Need to wait @JInchi finished the new Fragment, and then just read the @param destination info from the mainViewModel.
    private final String destination = "fakeDestination";
    /**/

    public UserInfo() {
    }

    public UserInfo(String id, String name, String phone, String des) {
        this.id = id;
        this.name = name;
        phoneNum = phone;
        // TODO
        //destination = des;
    }

    public String getId() {
        return id;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", gender='" + gender + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
