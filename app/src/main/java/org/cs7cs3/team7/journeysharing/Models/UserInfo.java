package org.cs7cs3.team7.journeysharing.Models;

public class UserInfo {
    // User Info
    private String id;
    private String name;
    private String phoneNum;
    private String gender;

    public UserInfo(String id, String name, String phone, String gender) {
        this.id = id;
        this.name = name;
        this.phoneNum = phone;
        this.gender = gender;
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
