package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class VideoFrame {
    private byte[] data;
    private ByteArrayInputStream processedInputStream;

    public VideoFrame(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public ByteArrayInputStream getProcessedInputStream() {
        return processedInputStream;
    }

    public void setProcessedInputStream(ByteArrayInputStream processedInputStream) {
        this.processedInputStream = processedInputStream;
    }
}
