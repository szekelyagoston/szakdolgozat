package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

public class FacePPHeadPose {

    @JsonProperty("yaw_angle")
    private Long yawAngle;
    @JsonProperty("pitch_angle")
    private Long pitchAngle;
    @JsonProperty("roll_angle")
    private Long rollAngle;

    public Long getYawAngle() {
        return yawAngle;
    }

    public void setYawAngle(Long yawAngle) {
        this.yawAngle = yawAngle;
    }

    public Long getPitchAngle() {
        return pitchAngle;
    }

    public void setPitchAngle(Long pitchAngle) {
        this.pitchAngle = pitchAngle;
    }

    public Long getRollAngle() {
        return rollAngle;
    }

    public void setRollAngle(Long rollAngle) {
        this.rollAngle = rollAngle;
    }
}
