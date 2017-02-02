package com.fbauth.checAuth.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleModel implements Parcelable {
    private String modelString;
    private String modelImage;
    private String modelDate;
    private String modelState;

    private ArrayList<SampleModelHistory> modelHistory;

    enum state {
        AVAILABLE,
        BLOCKED,
        ENAGAGED
    }

    public SampleModel(String mString, String mImage, String mDate, String mState) {
        this.modelString = mString;
        this.modelImage = mImage;
        this.modelDate = mDate;
        this.modelState = mState;
    }

    protected SampleModel(Parcel in) {
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

    public static final Creator<SampleModel> CREATOR = new Creator<SampleModel>() {
        @Override
        public SampleModel createFromParcel(Parcel in) {
            return new SampleModel(in);
        }

        @Override
        public SampleModel[] newArray(int size) {
            return new SampleModel[size];
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

    public ArrayList<SampleModelHistory> getModelHistory() {
        return modelHistory;
    }

    public void setModelHistory(ArrayList<SampleModelHistory> modelHistory) {
        this.modelHistory = modelHistory;
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

        public SampleModel build() {
            return new SampleModel(modelString, modelImage, modelDate, modelState);
        }
    }
}
