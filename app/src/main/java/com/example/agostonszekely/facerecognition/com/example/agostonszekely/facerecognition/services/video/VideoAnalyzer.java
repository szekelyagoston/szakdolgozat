package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class VideoAnalyzer implements IVideoAnalyzer{
    private List<VideoFrame> frames = new ArrayList<>();
    private Camera.Parameters parameters;
    private int width;
    private int height;

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
    public List<Bitmap> processData() {

        int steps = frames.size() / 10 + 1;

        List<Bitmap> result = new ArrayList<>(frames.size());

        for (int i = 0; i < frames.size(); i = i + steps){

            VideoFrame frame = frames.get(i);

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
}
