package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services;

/**
 * Created by Ágoston on 05/11/2016.
 */

public interface AsyncResponseWithIndex<T> {
    void processFinish(T output, int index);
}
