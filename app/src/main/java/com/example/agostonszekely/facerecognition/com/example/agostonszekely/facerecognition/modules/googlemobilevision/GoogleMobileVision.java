package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.BaseFaceRecognitionApi;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.19..
 */

public class GoogleMobileVision extends BaseFaceRecognitionApi implements IFaceRecognition {

    public GoogleMobileVision(AsyncResponse<List<FaceProperties>> delegate) {
        super(delegate);
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream) throws NoContextPresentException {
        throw new NoContextPresentException("Google Mobile Vision needs the application context!");
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream, Context context) {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true)
                .build();

        //creating a bitmap from our inputstream
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        //creating a frame from our bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Face> faces;
        if (detector.isOperational()){
            //detecting faces synchronously
            faces = detector.detect(frame);

            List<FaceProperties> resultArray = new ArrayList<>();
            //iterating through sparseArray -> bit strange but it should work like this
            for (int i = 0; i < faces.size(); ++i){
                int key = faces.keyAt(i);
                Face face = faces.get(key);
                FaceRectangle rectangle = new FaceRectangle((int)face.getPosition().x, (int)face.getPosition().y, (int)face.getPosition().x +  (int)face.getWidth(), (int)face.getPosition().y +  (int)face.getHeight());

                FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw((double)face.getEulerY());
                FacePosition position = new FacePosition((double)face.getEulerY(), facePosition);
                resultArray.add(new FaceProperties(rectangle, position));
            }
            detector.release();
            delegate.processFinish(resultArray);
        }else{
            delegate.processFinish(new ArrayList<FaceProperties>());
        }




    }
}
