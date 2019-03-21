package org.cs7cs3.team7.journeysharing.Models;

import org.cs7cs3.team7.journeysharing.MainViewModel;

import androidx.lifecycle.ViewModel;

public class UserInfo {
    // User Info
    private String name;
    private String phoneNum;
    private String gender;
    private int uniqueID;

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

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "name= " + name + "\nphoneNum= " + phoneNum + "\n";
    }

}
