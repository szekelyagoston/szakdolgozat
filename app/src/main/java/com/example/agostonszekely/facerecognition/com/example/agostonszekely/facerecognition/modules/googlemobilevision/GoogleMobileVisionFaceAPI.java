package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public class GoogleMobileVisionFaceAPI implements Callable<List<FaceProperties>> {

    //stream of one single picture
    private ByteArrayInputStream stream;

    private Context context;

    public GoogleMobileVisionFaceAPI(ByteArrayInputStream stream, Context context){
        this.stream = stream;
        this.context = context;
    }

    @Override
    public List<FaceProperties> call() throws Exception {
        FaceDetector detector = new FaceDetector.Builder(context)
                .setProminentFaceOnly(true)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        //creating a bitmap from our inputstream
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        //creating a frame from our bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        List<FaceProperties> resultArray = new ArrayList<>();
        SparseArray<Face> faces;
        if (detector.isOperational()){
            faces = detector.detect(frame);
            for (int i = 0; i < faces.size(); ++i){
                int key = faces.keyAt(i);
                Face face = faces.get(key);

                FaceRectangle rectangle = new FaceRectangle((int)face.getPosition().x, (int)face.getPosition().y, (int)face.getPosition().x +  (int)face.getWidth(), (int)face.getPosition().y +  (int)face.getHeight());

                double eulerY = (double) face.getEulerY();
                FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw(eulerY);
                FacePosition position = new FacePosition(eulerY, facePosition);
                resultArray.add(new FaceProperties(rectangle, position));
            }
        }

        return resultArray;
    }
}
