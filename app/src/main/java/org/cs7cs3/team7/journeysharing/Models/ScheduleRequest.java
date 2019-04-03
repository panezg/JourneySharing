package org.cs7cs3.team7.journeysharing.Models;

import com.google.gson.annotations.Expose;

public class ScheduleRequest {
    @Expose
    private String scheduleDateTime;
    @Expose
    private int userId;
    @Expose
    private String startPosition;
    @Expose
    private String startPositionLongitude;
    @Expose
    private String startPositionLatitude;
    @Expose
    private String endPosition;
    @Expose
    private String endPositionLongitude;
    @Expose
    private String endPositionLatitude;
    @Expose
    private int genderPreference;
    @Expose
    private int commuteType;

    public String getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getStartPositionLongitude() {
        return startPositionLongitude;
    }

    public void setStartPositionLongitude(String startPositionLongitude) {
        this.startPositionLongitude = startPositionLongitude;
    }

    public String getStartPositionLatitude() {
        return startPositionLatitude;
    }

    public void setStartPositionLatitude(String startPositionLatitude) {
        this.startPositionLatitude = startPositionLatitude;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getEndPositionLongitude() {
        return endPositionLongitude;
    }

    public void setEndPositionLongitude(String endPositionLongitude) {
        this.endPositionLongitude = endPositionLongitude;
    }

    public String getEndPositionLatitude() {
        return endPositionLatitude;
    }

    public void setEndPositionLatitude(String endPositionLatitude) {
        this.endPositionLatitude = endPositionLatitude;
    }

    public int getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(int genderPreference) {
        this.genderPreference = genderPreference;
    }

    public int getCommuteType() {
        return commuteType;
    }

    public void setCommuteType(int commuteType) {
        this.commuteType = commuteType;
    }
}
