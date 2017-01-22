package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.BaseFaceRecognitionApi;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.rest.FacePPApi;
import com.google.android.gms.vision.face.Face;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by agoston.szekely on 2017.01.20..
 */

public class FacePlusPlus extends BaseFaceRecognitionApi implements IFaceRecognition {

    private static final String API_KEY = "IKfHyIk6jzb1MIQHSul4UnVa4e8S9WLj";
    private static final String API_SECRET = "VUWTC48yIo06iJ6hzz8LX89E9l5RNyfc";

    private static final String API_DETECT_PATH = "detect";

    public FacePlusPlus(AsyncResponse<List<FaceProperties>> delegate) {
        super(delegate);
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream) {

        //Creating a bitmap
        Bitmap src = BitmapFactory.decodeStream(inputStream);

        //Bitmap bitmap = BitmapProducer.CreateBitmap(src, rotationMatrix);
        //Bitmap bitmap = BitmapProducer.CreateBitmap(src, new Matrix());
        //change to bytearrayinputstream again
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("api_secret", API_SECRET);
        params.put("image_file", new ByteArrayInputStream(byteArray));
        //params.put("image_url", "http://inst.eecs.berkeley.edu/~cs194-26/fa15/upload/files/proj5/cs194-bl/images/obama.jpg");
        params.put("return_attributes", "headpose");

        FacePPApi.post(API_DETECT_PATH, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                FacePPResponse faceResponse = null;
                List<FacePPFaces> faces = null;
                try {
                    faceResponse = mapper.readValue(response.toString(), FacePPResponse.class);
                    faces = faceResponse.getFaces();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<FaceProperties> resultArray = new ArrayList<>();
                //iterating through sparseArray -> bit strange but it should work like this
                /*for (int i = 0; i < faces.size(); ++i){
                    int key = faces.keyAt(i);
                    Face face = faces.get(key);
                    FaceRectangle rectangle = new FaceRectangle((int)face.getPosition().x, (int)face.getPosition().y, (int)face.getPosition().x +  (int)face.getWidth(), (int)face.getPosition().y +  (int)face.getHeight());
                    double eulerY = (double) face.getEulerY();
                    FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw(eulerY);
                    FacePosition position = new FacePosition(eulerY, facePosition);
                    resultArray.add(new FaceProperties(rectangle, position));
                }*/

                delegate.processFinish(resultArray);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private byte[] convertToByteArray(ByteArrayInputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        try {
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toByteArray();
    }

    @Override
    public void getFaceRectangle(ByteArrayInputStream inputStream, Context context) {
        this.getFaceRectangle(inputStream);
    }
}
