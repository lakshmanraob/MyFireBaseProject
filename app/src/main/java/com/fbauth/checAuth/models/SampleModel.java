package com.fbauth.checAuth.models;

import java.util.ArrayList;

/**
 * Created by labattula on 19/01/17.
 */

public class SampleModel {
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
}
