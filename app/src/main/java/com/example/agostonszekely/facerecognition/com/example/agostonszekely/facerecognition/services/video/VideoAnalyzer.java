package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.ChallengeResult;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.challenge.utils.ChallengeTypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class VideoAnalyzer implements IVideoAnalyzer{

    private static final int MAX_SELECTED_FRAMES = 20;

    private List<VideoFrame> frames = new ArrayList<>();
    private Camera.Parameters parameters;
    private int width;
    private int height;

    Producer producer;
    Consumer consumer;

    public VideoAnalyzer(Camera.Parameters parameters) {
        this.parameters = parameters;
        this.height = parameters.getPreviewSize().height;
        this.width = parameters.getPreviewSize().width;
    }

    public List<VideoFrame> getFrames() {
        return frames;
    }

    @Override
    public void addFrame(VideoFrame frame){
        this.frames.add(frame);
    }

    @Override
    public List<Bitmap> processDataWithBitmaps() {


        //maximum MAX_SELECTED_FRAMES frames should be selected
        float steps;
        int resultCount;
        //we have less than MAX_SELECTED_FRAMES frames to choose from.
        if (frames.size() < MAX_SELECTED_FRAMES){
            steps = 1;
            resultCount = frames.size();
        }else{
            //we have more than MAX_SELECTED_FRAMES frames, so we choose MAX_SELECTED_FRAMES.
            steps = frames.size() / MAX_SELECTED_FRAMES;
            resultCount = MAX_SELECTED_FRAMES;
        }

        List<Bitmap> result = new ArrayList<>(MAX_SELECTED_FRAMES);

        float currentValue = 0f;
        for (int i = 0; i < resultCount; ++i){
            currentValue = currentValue + steps;
            int nextIndex = ((int)currentValue) - 1;

            VideoFrame frame = frames.get(nextIndex);

            YuvImage yuv = new YuvImage(frame.getData(), parameters.getPreviewFormat(), width, height, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

            byte[] bytes = out.toByteArray();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Matrix rotationMatrix = null;
            try {
                rotationMatrix = OrientationHelper.rotate270();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapProducer.CreateBitmap(bitmap, rotationMatrix);
            bitmap = BitmapProducer.MirrorBitmap(bitmap);

            result.add(bitmap);


        }

        return result;
    }

    @Override
    public void processData(ChallengeTypes challenge, AsyncResponse response) {
        ChallengeResult result = new ChallengeResult(challenge);
        setupVideoAnalyzing();
        response.processFinish(result);
    }

    private void setupVideoAnalyzing() {
        BlockingQueue queue = new ArrayBlockingQueue(MAX_SELECTED_FRAMES);
        producer = new Producer(frames, queue);
        consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
