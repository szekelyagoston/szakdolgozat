package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePositionHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangle;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.rest.FacePPApi;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils.okhttp.RequestBodyUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by agoston.szekely on 2017.01.24..
 */

public class FacePPFaceAPI implements Callable<List<FaceProperties>> {

    private static final String API_KEY = "IKfHyIk6jzb1MIQHSul4UnVa4e8S9WLj";
    private static final String API_SECRET = "VUWTC48yIo06iJ6hzz8LX89E9l5RNyfc";

    private static final String OKHTTP_DETECT_PATH = "https://api-us.faceplusplus.com/facepp/v3/detect";
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("image/jpeg");

    private OkHttpClient client = new OkHttpClient();

    //stream of one single picture
    private ByteArrayInputStream stream;

    public FacePPFaceAPI(ByteArrayInputStream stream){
        this.stream = stream;

    }

    @Override
    public List<FaceProperties> call() throws Exception {

        Bitmap src = BitmapFactory.decodeStream(stream);

        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, bytearrayoutputstream);
        byte[] byteArray = bytearrayoutputstream.toByteArray();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_key", API_KEY)
                .addFormDataPart("api_secret", API_SECRET)
                .addFormDataPart("return_attributes", "headpose")
                .addFormDataPart("image_file", "image_file", RequestBody.create(MEDIA_TYPE_MARKDOWN, byteArray))
                .build();

        Request request = new Request.Builder()
                .url(OKHTTP_DETECT_PATH)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        List<FaceProperties> resultArray = new ArrayList<>();
        if (response.isSuccessful()){
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            FacePPResponse faceResponse = null;
            List<FacePPFaces> faces = null;


            faceResponse = mapper.readValue(response.body().string(), FacePPResponse.class);
            faces = faceResponse.getFaces();


            resultArray = new ArrayList<>();
            for (FacePPFaces face: faces){
                FaceRectangle rectangle = new FaceRectangle(face.getFaceRectangle().getLeft(), face.getFaceRectangle().getTop(), face.getFaceRectangle().getLeft() + face.getFaceRectangle().getWidth(), face.getFaceRectangle().getTop() + face.getFaceRectangle().getHeight());

                FacePositionEnum facePosition = FacePositionHelper.getFacePositionFromYaw(face.getFaceAttributes().getHeadPose().getCorrectedYawAngle());
                FacePosition position = new FacePosition(face.getFaceAttributes().getHeadPose().getCorrectedYawAngle(), facePosition);
                resultArray.add(new FaceProperties(rectangle, position));
            }
            System.out.println("SZÁJZ: "+resultArray.size());
        }
        if (!response.isSuccessful()){
            System.out.println("FAILED!!: " + response.body().string());
        }




        return resultArray;

    }



}
