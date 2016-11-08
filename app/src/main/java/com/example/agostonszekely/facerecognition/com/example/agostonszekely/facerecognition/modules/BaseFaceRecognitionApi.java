package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponseWithIndex;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public abstract class BaseFaceRecognitionApi<T>{
    public AsyncResponse delegate = null;
    public AsyncResponseWithIndex delegateWithIndex = null;

    protected int frameIndex;

    public BaseFaceRecognitionApi(AsyncResponse delegate){
        this.delegate = delegate;
    }
    public BaseFaceRecognitionApi(AsyncResponseWithIndex delegate){
        this.delegateWithIndex = delegate;
    }

    protected void processFinish(T output){
        if (delegate != null){
            delegate.processFinish(output);
        }
        if (delegateWithIndex != null){
            delegateWithIndex.processFinish(output, frameIndex);
        }
    }
}
