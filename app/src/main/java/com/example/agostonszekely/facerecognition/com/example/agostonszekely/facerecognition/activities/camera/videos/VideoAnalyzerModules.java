package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public enum VideoAnalyzerModules {
    MICROSOFT_PROJECT_OXFORD(0), GOOGLE_MOBILE_VISION(0), FACE_PP(1000);


    VideoAnalyzerModules(int delayTime) {
        this.delayTime = delayTime;
    }

    //delaying between calls (in millis). Might be userful when concurency limit exist
    private int delayTime;

    public int getDelayTime() {
        return delayTime;
    }
}
