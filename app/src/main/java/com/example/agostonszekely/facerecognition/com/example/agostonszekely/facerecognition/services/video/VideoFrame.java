package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class VideoFrame {
    private byte[] data;

    public VideoFrame(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
