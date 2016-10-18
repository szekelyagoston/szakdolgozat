package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public interface AsyncResponse<T> {
    void processFinish(T output);
}
