package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public class FacePosition {
    private Double yaw;
    private FacePositionEnum facePosition;

    public FacePosition(Double yaw, FacePositionEnum facePosition) {
        this.yaw = yaw;
        this.facePosition = facePosition;
    }

    public Double getYaw() {
        return yaw;
    }

    public FacePositionEnum getFacePosition() {
        return facePosition;
    }

    public void setFacePosition(FacePositionEnum facePosition) {
        this.facePosition = facePosition;
    }
}
