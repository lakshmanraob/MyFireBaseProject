package com.fbauth.checAuth.models;

/**
 * Created by labattula on 28/10/16.
 */

public class User extends FBObject {

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
