package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public enum FacePositionEnum {

    FACING_CAMERA(-15d, +15d), LOOKING_RIGHT(-90d, -15d), LOOKING_LEFT(+15d, +90d);

    private Double from;
    private Double to;

    FacePositionEnum(Double from, Double to) {
        this.from = from;
        this.to = to;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public String getReadableName(){
        switch(this){
            case FACING_CAMERA: {
                return "FACING CAMERA";
            }
            case LOOKING_LEFT: {
                return "LOOKING LEFT";
            }
            case LOOKING_RIGHT: {
                return "LOOKING RIGHT";
            }
        }

        return "CAN NOT DECIDE";
    }
}
