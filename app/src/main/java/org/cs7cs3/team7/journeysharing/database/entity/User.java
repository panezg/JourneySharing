package org.cs7cs3.team7.journeysharing.database.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    @PrimaryKey
    @NonNull
    private int id;
    private String login;
    @Expose
    @SerializedName("userName")
    private String names;
    @Expose
    @SerializedName("phoneNumber")
    private String phoneNum;
    private String gender;
    @Expose
    @SerializedName("gender")
    private int genderCode;
    private Date lastRefresh;

    // --- Constructors ---

    public User(int id, String login, String names, String phoneNum, String gender) {
        this.id = id;
        this.login = login;
        this.names = names;
        this.phoneNum = phoneNum;
        this.setGender(gender);
    }

    // --- Other Methods ---

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", names='" + names + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", gender='" + gender + '\'' +
                ", genderCode=" + genderCode +
                ", lastRefresh=" + lastRefresh +
                '}';
    }

    public String toMatchResultString() {
        return "Name: " + names +
                ", Phone #: " + phoneNum +
                ", Gender: " + gender;
    }

    // --- Setters and Getters

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
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
        if (gender.equals("Male")) {
            this.genderCode = GENDER_MALE;
        }
        else if (gender.equals("Female"))  {
            this.genderCode = GENDER_FEMALE;
        }
    }

    public int getGenderCode() {
        return this.genderCode;
    }

    public void setGenderCode(int genderCode) {
        this.genderCode = genderCode;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
