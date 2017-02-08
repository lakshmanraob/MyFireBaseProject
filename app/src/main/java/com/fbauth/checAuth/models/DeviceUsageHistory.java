package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by labattula on 06/02/17.
 */

public class DeviceUsageHistory implements Parcelable, Comparable<DeviceUsageHistory> {

    public static String PROFILE_ID = "profileId";
    public static String START_TIME = "startTime";
    public static String END_TIME = "endTime";

    String profileId;
    String startTime;
    String endTime;

    public DeviceUsageHistory() {

    }

    protected DeviceUsageHistory(Parcel in) {
        profileId = in.readString();
        startTime = in.readString();
        endTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileId);
        dest.writeString(startTime);
        dest.writeString(endTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceUsageHistory> CREATOR = new Creator<DeviceUsageHistory>() {
        @Override
        public DeviceUsageHistory createFromParcel(Parcel in) {
            return new DeviceUsageHistory(in);
        }

        @Override
        public DeviceUsageHistory[] newArray(int size) {
            return new DeviceUsageHistory[size];
        }
    };

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    @Override
    public int compareTo(DeviceUsageHistory comparedTo) {
        if (getLong(getStartTime()) > getLong(comparedTo.getStartTime())) {
            return -1;
        } else if (getLong(getStartTime()) < getLong(comparedTo.getStartTime())) {
            return 1;
        }
        return 0;
    }

    private long getLong(String str) {
        long longDate = Long.parseLong(str);
        //Date date = new Date(longDate);
        return longDate;
    }
}
