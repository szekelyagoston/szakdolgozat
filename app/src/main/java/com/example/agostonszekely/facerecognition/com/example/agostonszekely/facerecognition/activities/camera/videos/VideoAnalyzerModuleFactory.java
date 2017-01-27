package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.content.Context;
import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.exceptions.GoogleMobileVisionMissingContextException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp.FacePPFaceAPI;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.GoogleMobileVisionFaceAPI;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxfordFaceAPI;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.IVideoAnalyzer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoAnalyzer;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public class VideoAnalyzerModuleFactory {
    public Callable<List<FaceProperties>> getModule(VideoAnalyzerModules module, ByteArrayInputStream inputStream) throws GoogleMobileVisionMissingContextException {
        if (module.equals(VideoAnalyzerModules.GOOGLE_MOBILE_VISION)){
            throw new GoogleMobileVisionMissingContextException("Google Mobile Vision must be initialized wiht additional context information!");
        }
        return this.getModule(module, inputStream, null);
    }


    public Callable<List<FaceProperties>> getModule(VideoAnalyzerModules module, ByteArrayInputStream inputStream, Context context){
        switch (module){
            case GOOGLE_MOBILE_VISION: {
                return new GoogleMobileVisionFaceAPI(inputStream, context);
            }
            case MICROSOFT_PROJECT_OXFORD: {
                return new MicrosoftProjectOxfordFaceAPI(inputStream);
            }
            case FACE_PP: {
                return new FacePPFaceAPI(inputStream);
            }
            default: {
                return null;
            }
        }
    }

    public IVideoAnalyzer getVideoAnalyzer(VideoAnalyzerModules module, Camera.Parameters parameters, Context context){
        switch (module){
            case GOOGLE_MOBILE_VISION: {
                return new GMOVideoAnalyzer(parameters, context);
            }
            case MICROSOFT_PROJECT_OXFORD: {
                return new MPOVideoAnalyzer(parameters);
            }
            case FACE_PP: {
                return new FacePPVideoAnalyzer(parameters);
            }
            default: {
                return null;
            }
        }
    }
}
