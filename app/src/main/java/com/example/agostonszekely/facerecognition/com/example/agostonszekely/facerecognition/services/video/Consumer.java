package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;

import java.util.concurrent.BlockingQueue;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class Consumer implements Runnable{

    private BlockingQueue<IFaceRecognition> queue;

    public Consumer(BlockingQueue<IFaceRecognition> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true) {
            try {
                //nem jó, it tmár az eredményt kéne kivenni
                IFaceRecognition result = queue.take();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
