package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxfordFaceAPI;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoAnalyzer;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public class MPOVideoAnalyzer extends VideoAnalyzer {

    public MPOVideoAnalyzer(Camera.Parameters parameters) {
        super(parameters, VideoAnalyzerModules.MICROSOFT_PROJECT_OXFORD);
    }
}
