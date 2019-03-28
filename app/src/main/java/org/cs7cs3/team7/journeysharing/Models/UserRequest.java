package org.cs7cs3.team7.journeysharing.Models;

public class UserRequest {
    private String phoneNumber;
    private int gender;
    private String userName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserRequest(String userName, int gender, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.userName = userName;
    }

    public UserRequest() {
    }
}
