package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;

import java.io.ByteArrayInputStream;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public interface IFaceRecognition {
    void getFaceRectangle(ByteArrayInputStream inputStream);
}
