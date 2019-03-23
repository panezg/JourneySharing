package org.cs7cs3.team7.journeysharing.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MatchingResultInfo implements Parcelable {

    public enum MatchingResultStatus {MATCHED, NO_MATCH}

    // General fields
    private List<UserInfo> groupMembers;

    // Fields for online mode
    private String meetingDate;
    private String meetingTime;
    private String meetingPlace;
    private MatchingResultStatus status;

    public MatchingResultInfo(List<UserInfo> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public List<UserInfo> getGroupMembers() {
        return groupMembers;
    }

    public MatchingResultStatus getStatus() {
        return status;
    }

    public void setStatus(MatchingResultStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MatchingResultInfo{" +
                "groupMembers=" + groupMembers +
                ", meetingDate='" + meetingDate + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingPlace='" + meetingPlace + '\'' +
                ", status=" + status +
                '}';
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MatchingResultInfo createFromParcel(Parcel in) {
            return new MatchingResultInfo(in);
        }

        public MatchingResultInfo[] newArray(int size) {
            return new MatchingResultInfo[size];
        }
    };

    public MatchingResultInfo(Parcel in) {
        this.groupMembers = (List<UserInfo>) in.readArrayList(UserInfo.class.getClassLoader());
        this.meetingDate = in.readString();
        this.meetingTime = in.readString();
        this.meetingPlace = in.readString();
        String status = in.readString();
        this.status = MatchingResultStatus.valueOf(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.groupMembers);
        dest.writeString(this.meetingDate);
        dest.writeString(this.meetingTime);
        dest.writeString(this.meetingPlace);
        dest.writeString(this.status.toString());
    }
}
