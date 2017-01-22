package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

public class FacePPAttributes {

    @JsonProperty("headpose")
    private FacePPHeadPose headPose;

    public FacePPHeadPose getHeadPose() {
        return headPose;
    }

    public void setHeadPose(FacePPHeadPose headPose) {
        this.headPose = headPose;
    }
}
