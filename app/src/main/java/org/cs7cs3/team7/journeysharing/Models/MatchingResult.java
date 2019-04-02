package org.cs7cs3.team7.journeysharing.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.List;

public class MatchingResult implements Parcelable {

    public enum MatchingResultStatus {MATCHED, NO_MATCH}

    // General fields
    private List<User> groupMembers;

    // Fields for online mode
    private String meetingDate;
    private String meetingTime;
    private String meetingPlace;
    private MatchingResultStatus status;

    public MatchingResult(List<User> groupMembers) {
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

    public List<User> getGroupMembers() {
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
        return "MatchingResult{" +
                "groupMembers=" + groupMembers +
                ", meetingDate='" + meetingDate + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingPlace='" + meetingPlace + '\'' +
                ", status=" + status +
                '}';
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MatchingResult createFromParcel(Parcel in) {
            return new MatchingResult(in);
        }

        public MatchingResult[] newArray(int size) {
            return new MatchingResult[size];
        }
    };

    public MatchingResult(Parcel in) {
        this.groupMembers = (List<User>) in.readArrayList(User.class.getClassLoader());
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
