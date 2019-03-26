package org.cs7cs3.team7.journeysharing.Models;

public class JourneyRequestInfo {

    public static enum JourneyRequestStatus {
        FINISHED,
        SCHEDULED,
        PENDING,
    }

    private JourneyRequestStatus state;

    // Time scheduled
    private String date;
    private String time;

    // Preferences
    private String gender;
    private String method;

    // Scheduled places
    private String destination;
    private String startPoint;

    private UserInfo userInfo;

    public JourneyRequestInfo(UserInfo userInfo, String gender, String method, String destination) {
        this.userInfo = userInfo;
        this.gender = gender;
        this.method = method;
        this.destination = destination;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public JourneyRequestStatus getState() {
        return state;
    }

    public void setState(JourneyRequestStatus state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGender() {
        return gender;
    }

    public String getMethod() {
        return method;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    @Override
    public String toString() {
        return "JourneyRequestInfo{" +
                "state=" + state +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", gender='" + gender + '\'' +
                ", method='" + method + '\'' +
                ", destination='" + destination + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
