package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponseWithIndex;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceRecognitionTask;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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

        for (int i = 0; i < frames.size(); ++i) {

            //calling Microsoft Oxford API
            new MicrosoftProjectOxford(i, new AsyncResponseWithIndex<List<FaceProperties>>() {

                @Override
                public void processFinish(List<FaceProperties> faces, int index) {

                    FaceRecognitionTask faceRecognitionTask = processedData.get(index);
                    faceRecognitionTask.setFinished(Boolean.TRUE);
                    faceRecognitionTask.setFaces(faces);
                    if (faces.size() > 0) {
                        //WE SHOULD DO SOMETHING HERE MAYBE?
                        processedData.add(index, faceRecognitionTask);
                    } else {
                        processedData.add(index, faceRecognitionTask);
                    }

                }
            }).getFaceRectangle(frames.get(i).getProcessedInputStream());
        }

        startCheckingForFinish(response);
        //response.processFinish(new ArrayList<List<FaceProperties>>());
    }

    private void startCheckingForFinish(AsyncResponse<List<List<FaceProperties>>> response) {
        while(true){
            Boolean foundNotProcessed = false;
            for (int i = 0; i < processedData.size() && !foundNotProcessed; ++i){
                foundNotProcessed = !processedData.get(i).getFinished();
            }

            if (!foundNotProcessed){

                List<List<FaceProperties>> result = new ArrayList<>();
                for (FaceRecognitionTask faceProperties : processedData){
                    result.add(faceProperties.getFaces());
                }
                response.processFinish(result);
                break;
            }
        }

    }
}
