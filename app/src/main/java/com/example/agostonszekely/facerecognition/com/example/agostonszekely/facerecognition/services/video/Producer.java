package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class Producer implements Runnable{
    private BlockingQueue<IFaceRecognition> queue = null;
    private List<VideoFrame> frames;

    public Producer(List<VideoFrame> frames, BlockingQueue<IFaceRecognition> queue) {
        this.frames = frames;
        this.queue = queue;
    }

    @Override
    public void run(){
        for (VideoFrame frame : frames){
            IFaceRecognition faceRecognition;

            faceRecognition = new MicrosoftProjectOxford(new AsyncResponse<List<FaceProperties>>() {
                @Override
                public void processFinish(List<FaceProperties> faces) {
                    if (faces.size() >0){

                    }
                }
            });

            try {
                queue.put(faceRecognition.getFaceRectangle(frame.getData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
