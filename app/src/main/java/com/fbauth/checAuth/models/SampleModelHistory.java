package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by labattula on 20/01/17.
 */

public class SampleModelHistory implements Parcelable {

    String user;
    String starttime;
    String endtime;

    public SampleModelHistory() {

    }

    protected SampleModelHistory(Parcel in) {
        user = in.readString();
        starttime = in.readString();
        endtime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(starttime);
        dest.writeString(endtime);
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
