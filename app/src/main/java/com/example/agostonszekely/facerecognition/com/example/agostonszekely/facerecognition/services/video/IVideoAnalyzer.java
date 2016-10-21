package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public interface IVideoAnalyzer {
    void addFrame(VideoFrame frame);
    List<Bitmap> processData();
}
