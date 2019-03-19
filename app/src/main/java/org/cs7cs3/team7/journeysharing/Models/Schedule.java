package org.cs7cs3.team7.journeysharing.Models;

import java.util.Date;

public class Schedule {
    private Integer userId;
    private String startPosition;
    private String startPositionLongitude;
    private String startPositionLatitude;
    private String endPosition;
    private String endPositionLongitude;
    private String endPositionLatitude;
    private Date when;
    private Integer type;

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

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }
}
