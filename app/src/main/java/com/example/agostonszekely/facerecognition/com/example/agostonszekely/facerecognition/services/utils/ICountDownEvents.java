package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public interface ICountDownEvents {
    void onTick(int secondsRemaining);
    void onFinish();
}
