package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford;

import android.os.AsyncTask;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Ágoston on 08/11/2016.
 */

public class MicrosoftProjectOxfordFaceAPI implements Callable<List<FaceProperties>> {

    private ByteArrayInputStream stream;

    private FaceServiceClient faceServiceClient =
            new FaceServiceRestClient("8976f015460d43f6b99ebcf989dabadd");

    public MicrosoftProjectOxfordFaceAPI(ByteArrayInputStream stream){
        this.stream = stream;

    }

    @Override
    public List<FaceProperties> call() throws Exception {

        Face[] result = faceServiceClient.detect(
                stream,
                true,         // returnFaceId
                false,        // returnFaceLandmarks
                new FaceServiceClient.FaceAttributeType[]{FaceServiceClient.FaceAttributeType.HeadPose}       // returnFaceAttributes: a string like "age, gender"
        );
        if (result == null)
        {
            return null;
        }

        List<FaceProperties> resultArray = new ArrayList<>();
        for (Face face : result){
            FaceRectangle rectangle = new FaceRectangle(face.faceRectangle.left, face.faceRectangle.top, face.faceRectangle.left + face.faceRectangle.width, face.faceRectangle.top + face.faceRectangle.height);

            FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw(face.faceAttributes.headPose.yaw);
            FacePosition position = new FacePosition(face.faceAttributes.headPose.yaw, facePosition);
            resultArray.add(new FaceProperties(rectangle, position));
        }

        return resultArray;
    }
}
