package org.cs7cs3.team7.journeysharing.Models;

public class ScheduledJourneyInfo {

    ScheduledJourneyType state;

    // Time scheduled
    private String date;
    private String time;

    // Preferences
    private String gender;
    private String method;

    // Scheduled places
    private String destination;
    private String startPoint;

    public ScheduledJourneyType getState() {
        return state;
    }

    public void setState(ScheduledJourneyType state) {
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
}
