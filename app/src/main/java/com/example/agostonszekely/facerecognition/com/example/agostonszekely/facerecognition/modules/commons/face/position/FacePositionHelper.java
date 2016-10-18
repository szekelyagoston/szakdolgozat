package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public class FacePositionHelper {

    public static final FacePositionEnum getFacePositionFromYaw(Double yaw){
        if (yaw > FacePositionEnum.FACING_CAMERA.getFrom() && yaw < FacePositionEnum.FACING_CAMERA.getTo()){
            return FacePositionEnum.FACING_CAMERA;
        }
        if (yaw > FacePositionEnum.LOOKING_LEFT.getFrom() && yaw < FacePositionEnum.LOOKING_LEFT.getTo()){
            return FacePositionEnum.LOOKING_LEFT;
        }
        if (yaw > FacePositionEnum.LOOKING_RIGHT.getFrom() && yaw < FacePositionEnum.LOOKING_RIGHT.getTo()){
            return FacePositionEnum.LOOKING_RIGHT;
        }
        return null;
    }
}
