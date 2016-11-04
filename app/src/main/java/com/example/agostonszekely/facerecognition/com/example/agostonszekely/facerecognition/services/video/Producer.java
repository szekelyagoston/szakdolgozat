package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class Producer implements Runnable{
    private BlockingQueue<VideoFrame> queue = null;
    private List<VideoFrame> frames;

    public Producer(List<VideoFrame> frames, BlockingQueue<VideoFrame> queue) {
        this.frames = frames;
        this.queue = queue;
    }

    @Override
    public void run(){
        //adding frames to queue;
    }
}
