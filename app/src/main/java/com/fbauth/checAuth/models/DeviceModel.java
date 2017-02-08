package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by labattula on 19/01/17.
 */

public class DeviceModel implements Parcelable {

    public static final String SM_MODEL_STRING = "modelString";
    public static final String SM_MODEL_IMAGE = "modelImage";
    public static final String SM_MODEL_STATUS = "modelState";
    public static final String SM_MODEL_HISTORY = "history";

    public static final String SM_HT_USER = "user";
    public static final String SM_HT_START_TIME = "starttime";
    public static final String SM_HT_END_TIME = "endtime";

    public static final String SELECTED_DEVICE = "selectedDevice";

    private String modelString;
    private String modelImage;
    private String modelDate;
    private String modelState;

    enum state {
        AVAILABLE,
        BLOCKED,
        ENAGAGED
    }

    public DeviceModel() {

    }

    public DeviceModel(String mString, String mImage, String mDate, String mState) {
        this.modelString = mString;
        this.modelImage = mImage;
        this.modelDate = mDate;
        this.modelState = mState;
    }

    protected DeviceModel(Parcel in) {
        modelString = in.readString();
        modelImage = in.readString();
        modelDate = in.readString();
        modelState = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modelString);
        dest.writeString(modelImage);
        dest.writeString(modelDate);
        dest.writeString(modelState);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceModel> CREATOR = new Creator<DeviceModel>() {
        @Override
        public DeviceModel createFromParcel(Parcel in) {
            return new DeviceModel(in);
        }

        @Override
        public DeviceModel[] newArray(int size) {
            return new DeviceModel[size];
        }
    };

    public String getModelString() {
        return modelString;
    }

    public void setModelString(String modelString) {
        this.modelString = modelString;
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }

    public String getModelDate() {
        return modelDate;
    }

    public void setModelDate(String modelDate) {
        this.modelDate = modelDate;
    }

    public String getModelState() {
        return modelState;
    }

    public void setModelState(String modelState) {
        this.modelState = modelState;
    }

    public static class Builder {
        private String modelString;
        private String modelImage;
        private String modelDate;
        private String modelState;

        public Builder setModelString(String str) {
            this.modelString = str;
            return this;
        }

        public Builder setModelImage(String str) {
            this.modelImage = str;
            return this;
        }

        public Builder setModelDate(String str) {
            this.modelDate = str;
            return this;
        }

        public Builder setModelState(String str) {
            this.modelState = str;
            return this;
        }

        public DeviceModel build() {
            return new DeviceModel(modelString, modelImage, modelDate, modelState);
        }
    }
}
