package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by labattula on 28/10/16.
 */

public class Profile extends FBObject implements Parcelable {

    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_UDID = "userUDID";
    public static final String USER_PHONE = "phoneNumber";


    private String userName;
    private String userEmail;
    private String userUDID;
    private String phoneNumber;

    public Profile() {

    }

    public Profile(HashMap<String, String> profileMap) {
        if (profileMap.containsKey(USER_EMAIL)) {
            this.userEmail = profileMap.get(USER_EMAIL);
        }

        if (profileMap.containsKey(USER_NAME)) {
            this.userName = profileMap.get(USER_NAME);
        }

        if (profileMap.containsKey(USER_PHONE)) {
            this.phoneNumber = profileMap.get(USER_PHONE);
        }

        if (profileMap.containsKey(USER_UDID)) {
            this.userUDID = profileMap.get(USER_UDID);
        }
    }

    protected Profile(Parcel in) {
        userName = in.readString();
        userEmail = in.readString();
        userUDID = in.readString();
        phoneNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userUDID);
        dest.writeString(phoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserUDID() {
        return userUDID;
    }

    public void setUserUDID(String userUDID) {
        this.userUDID = userUDID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
