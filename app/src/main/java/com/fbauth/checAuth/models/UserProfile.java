package com.fbauth.checAuth.models;

/**
 * Created by labattula on 28/10/16.
 */

public class UserProfile extends FBObject {

    private String userName;
    private String userEmail;
    private String userUDID;
    private String phoneNumber;

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
