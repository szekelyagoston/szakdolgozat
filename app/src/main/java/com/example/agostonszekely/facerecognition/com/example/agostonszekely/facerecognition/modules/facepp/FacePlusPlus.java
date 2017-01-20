package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import android.content.Context;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.BaseFaceRecognitionApi;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.rest.FacePPApi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        params.put("api_secret", API_SECRET);
        params.put("image_file", convertToByteArray(inputStream));
        params.put("return_attributes", "headpose");

        FacePPApi.post(API_DETECT_PATH, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
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
