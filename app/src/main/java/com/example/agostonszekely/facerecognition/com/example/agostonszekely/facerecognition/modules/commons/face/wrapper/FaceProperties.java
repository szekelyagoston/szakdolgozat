package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public class FaceProperties {
    private FaceRectangle faceRectangle;

    private FacePosition facePosition;

    public FaceProperties(FaceRectangle faceRectangle, FacePosition facePosition) {
        this.facePosition = facePosition;
        this.faceRectangle = faceRectangle;
    }

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public FacePosition getFacePosition() {
        return facePosition;
    }
}
