package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public abstract class BaseFaceRecognitionApi{
    public AsyncResponse delegate = null;

    public BaseFaceRecognitionApi(AsyncResponse delegate){
        this.delegate = delegate;
    }
}
