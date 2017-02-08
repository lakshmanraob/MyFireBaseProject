package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by labattula on 20/01/17.
 */

public class SampleModelHistory implements Parcelable {


    String deviceName;
    ArrayList<DeviceUsageHistory> deviceUsageHistory;

    public SampleModelHistory() {

    }

    protected SampleModelHistory(Parcel in) {
        deviceName = in.readString();
        deviceUsageHistory = in.readParcelable(DeviceUsageHistory.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceName);
        dest.writeList(deviceUsageHistory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SampleModelHistory> CREATOR = new Creator<SampleModelHistory>() {
        @Override
        public SampleModelHistory createFromParcel(Parcel in) {
            return new SampleModelHistory(in);
        }

        @Override
        public SampleModelHistory[] newArray(int size) {
            return new SampleModelHistory[size];
        }
    };

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public ArrayList<DeviceUsageHistory> getDeviceUsageHistory() {
        return deviceUsageHistory;
    }

    public void setDeviceUsageHistory(ArrayList<DeviceUsageHistory> deviceUsageHistory) {
        this.deviceUsageHistory = deviceUsageHistory;
    }
}
