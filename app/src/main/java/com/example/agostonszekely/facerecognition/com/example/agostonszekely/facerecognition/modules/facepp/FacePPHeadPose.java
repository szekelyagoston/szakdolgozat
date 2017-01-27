package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

//FACE++ returns different yaw poses -> where microsoft and google returns positive, it returns negative. Original values will be saved
public class FacePPHeadPose {

    @JsonProperty("yaw_angle")
    private Double yawAngle;
    @JsonProperty("pitch_angle")
    private Double pitchAngle;
    @JsonProperty("roll_angle")
    private Double rollAngle;

    public Double getYawAngle() {
        return yawAngle;
    }

    public Double getCorrectedYawAngle(){
        return yawAngle * (-1);
    }

    public void setYawAngle(Double yawAngle) {
        this.yawAngle = yawAngle;
    }

    public Double getPitchAngle() {
        return pitchAngle;
    }

    public void setPitchAngle(Double pitchAngle) {
        this.pitchAngle = pitchAngle;
    }

    public Double getRollAngle() {
        return rollAngle;
    }

    public void setRollAngle(Double rollAngle) {
        this.rollAngle = rollAngle;
    }
}
