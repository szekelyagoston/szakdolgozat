package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import java.util.concurrent.BlockingQueue;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class Consumer implements Runnable{

    private BlockingQueue<VideoFrame> queue;

    public Consumer(BlockingQueue<VideoFrame> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true) {
            //queue.take();
        }
    }
}
