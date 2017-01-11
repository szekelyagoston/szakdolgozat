package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.exceptions.GoogleMobileVisionMissingContextException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.challenge.utils.ChallengeTypes;

import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public interface IVideoAnalyzer {
    void addFrame(VideoFrame frame);
    List<Bitmap> processDataWithBitmaps();
    void processData(ChallengeTypes challenge, AsyncResponse response) throws GoogleMobileVisionMissingContextException;
}
