package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.content.Context;
import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxfordFaceAPI;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoAnalyzer;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public class GMOVideoAnalyzer extends VideoAnalyzer{

    public GMOVideoAnalyzer(Camera.Parameters parameters, Context context) {
        super(parameters, VideoAnalyzerModules.GOOGLE_MOBILE_VISION, context);
    }
}
