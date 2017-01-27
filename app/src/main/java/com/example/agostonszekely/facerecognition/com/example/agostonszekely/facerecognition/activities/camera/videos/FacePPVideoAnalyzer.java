package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoAnalyzer;

/**
 * Created by agoston.szekely on 2017.01.24..
 */

public class FacePPVideoAnalyzer extends VideoAnalyzer{

    public FacePPVideoAnalyzer(Camera.Parameters parameters) {
        super(parameters, VideoAnalyzerModules.FACE_PP);
    }
}
