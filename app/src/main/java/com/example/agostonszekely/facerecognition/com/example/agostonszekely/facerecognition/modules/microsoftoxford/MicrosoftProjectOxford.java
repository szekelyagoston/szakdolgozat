package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford;

import android.content.Context;
import android.os.AsyncTask;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.BaseFaceRecognitionApi;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public class MicrosoftProjectOxford extends BaseFaceRecognitionApi implements IFaceRecognition{

    private static final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient("8976f015460d43f6b99ebcf989dabadd");

    public MicrosoftProjectOxford(AsyncResponse<List<FaceProperties>> delegate) {
        super(delegate);
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream) {

        //Passing class information for callbacks
        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    new FaceServiceClient.FaceAttributeType[]{FaceServiceClient.FaceAttributeType.HeadPose}       // returnFaceAttributes: a string like "age, gender"
                            );
                            if (result == null)
                            {
                                publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(
                                    String.format("Detection Finished. %d face(s) detected",
                                            result.length));
                            return result;
                        } catch (Exception e) {
                            publishProgress("Detection failed");
                            return null;
                        }
                    }
                    @Override
                    protected void onPreExecute() {
                        //detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        //detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                       //detectionProgressDialog.dismiss();
                        if (result == null) {
                            delegate.processFinish(new ArrayList<FaceProperties>());
                            return;
                        }

                        //Processing result
                        List<FaceProperties> resultArray = new ArrayList<>();
                        for (Face face : result){
                            FaceRectangle rectangle = new FaceRectangle(face.faceRectangle.left, face.faceRectangle.top, face.faceRectangle.left + face.faceRectangle.width, face.faceRectangle.top + face.faceRectangle.height);

                            FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw(face.faceAttributes.headPose.yaw);
                            FacePosition position = new FacePosition(face.faceAttributes.headPose.yaw, facePosition);
                            resultArray.add(new FaceProperties(rectangle, position));
                        }

                        delegate.processFinish(resultArray);
                    }

                };
        detectTask.execute(inputStream);
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream, Context context) {
        //No need to use context
        this.getFaceRectangle(inputStream);
    }

    ;

}
