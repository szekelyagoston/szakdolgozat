package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponseWithIndex;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceRecognitionTask;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxfordFaceAPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class FrameQueue{

    List<VideoFrame> frames;
    List<FaceRecognitionTask> processedData;

    public FrameQueue(List<VideoFrame> frames) {
        this.frames = frames;
        processedData = new ArrayList<>(frames.size());
        for (int i = 0; i < frames.size(); ++i){
            processedData.add(new FaceRecognitionTask());
        }
    }

    public void process(AsyncResponse<List<List<FaceProperties>>> response){
        ExecutorService executor = Executors.newFixedThreadPool(frames.size());
        List<Future<List<FaceProperties>>> result = new ArrayList<>(frames.size());
        List<List<FaceProperties>> allResult = new ArrayList<>(frames.size());

        for (VideoFrame frame : frames){
            Callable<List<FaceProperties>> callable = new MicrosoftProjectOxfordFaceAPI(frame.getProcessedInputStream());
            Future<List<FaceProperties>> future = executor.submit(callable);

            result.add(future);
        }

        for (Future<List<FaceProperties>> future : result){
            try {
                List<FaceProperties> faces = future.get();
                allResult.add(faces);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        response.processFinish(allResult);
    }


}
