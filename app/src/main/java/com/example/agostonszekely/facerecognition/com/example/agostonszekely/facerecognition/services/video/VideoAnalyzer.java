package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponseWithIndex;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;
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
    private List<List<FaceProperties>> processedFrames = new ArrayList<>();
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
    public void processData(final ChallengeTypes challenge,final AsyncResponse response) {

        List<VideoFrame> listForProcessing = processFramesForAPIFormat();

        FrameQueue frameQueue = new FrameQueue(listForProcessing);
        frameQueue.process(new AsyncResponse<List<List<FaceProperties>>>(){

            @Override
            public void processFinish(List<List<FaceProperties>> processedFaces) {
                ChallengeResult result = new ChallengeResult(challenge);

                int typeMultiplier = 1;
                if (challenge.equals(ChallengeTypes.TURN_HEAD_LEFT)){
                    typeMultiplier = -1;
                }

                for (List<FaceProperties> faces : processedFaces){
                    FaceProperties face;
                    if (faces.size()> 0){
                        face = faces.get(0);

                        if (face.getFacePosition().getYaw() * (typeMultiplier) > challenge.getFrom() * (typeMultiplier) ){
                            result.setAccepted(Boolean.TRUE);
                        }
                    }


                }

                response.processFinish(result);
            }
        });


    }

    private List<VideoFrame> processFramesForAPIFormat() {
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

        List<VideoFrame> framesForProcessing = new ArrayList<>(MAX_SELECTED_FRAMES);

        float currentValue = 0f;
        for (int i = 0; i < resultCount; ++i){
            currentValue = currentValue + steps;
            int nextIndex = ((int)currentValue) - 1;

            VideoFrame frame = frames.get(nextIndex);
            //calculating inputstream
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

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //compress quality -> if too high, method will be slow.
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 3, outputStream);
            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(outputStream.toByteArray());

            frame.setProcessedInputStream(inputStream);
            framesForProcessing.add(frame);

        }

        return framesForProcessing;
    }

}
