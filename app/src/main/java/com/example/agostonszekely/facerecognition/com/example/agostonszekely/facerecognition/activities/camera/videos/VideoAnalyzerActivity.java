package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.NavigationDrawerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.EnumHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.camera.CameraPreview;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.challenge.utils.ChallengeTypes;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils.CountDownType;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils.ICountDownEvents;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils.SecondCountDownTimer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.IVideoAnalyzer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoAnalyzer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.video.VideoFrame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class VideoAnalyzerActivity extends NavigationDrawerActivity {
    private final int MENUPOSITION = 4;
    private final static Integer COUNTDOWNSECONDS = 3;
    private final static Integer COUNTDOWNINTERVAL = 1;
    //means 20/10 -> 2sec
    private final static Integer TIMEOFCHALLENGE = 30;
    //Means 2/10 sec --> that way we will send 20 -1(due to the strange countdown bug) frames
    private final static Integer CHALLENGEFRAMECOUNT = 2;

    private Camera camera;
    private CameraPreview cameraPreview;

    private TextView textView;
    private SecondCountDownTimer timer;

    //progressdialog is shown when processing result
    private ProgressDialog detectionProgressDialog;

    private IVideoAnalyzer videoAnalyzer;
    private Boolean analyzeRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.video_analyzer);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);

        Button countStartButton = (Button) findViewById(R.id.button_challenge);
        textView = (TextView)findViewById(R.id.text_challenge);

        countStartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         startCountDown();
                    }
                }
        );

        detectionProgressDialog = new ProgressDialog(this);
        startCamera();
    }

    private void startCountDown() {
        textView.setText(Integer.toString(COUNTDOWNSECONDS));
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        setupCountDownTimer();

        timer.start();
    }

    private void setupCountDownTimer() {
        timer = new SecondCountDownTimer(COUNTDOWNSECONDS, COUNTDOWNINTERVAL, CountDownType.SECONDS, new ICountDownEvents() {

        @Override
        public void onTick(Integer secondsRemaining) {
            textView.setText(Integer.toString(secondsRemaining));
        }

        @Override
        public void onFinish() {
            startChallenge();
        }
    });
    }

    private void startChallenge() {
        ChallengeTypes challenge = EnumHelper.getRandomEnum(ChallengeTypes.class);
        textView.setText(challenge.getText());
        videoAnalyzer = new VideoAnalyzer(camera.getParameters());
        analyzeRunning = true;



        new SecondCountDownTimer(TIMEOFCHALLENGE, CHALLENGEFRAMECOUNT,CountDownType.TENTH_OF_SEC, new ICountDownEvents() {

            @Override
            public void onTick(Integer secondsRemaining) {
                //Do nothing
            }

            @Override
            public void onFinish() {
                analyzeRunning = false;
                //detectionProgressDialog.setMessage("Processing data");
                //detectionProgressDialog.show();

                //TEST
                releaseCamera();
                List<Bitmap> picturesFromVideo = videoAnalyzer.processData();

                testShowPicturesInOrder(picturesFromVideo);
                //detectionProgressDialog.dismiss();
            }

            
        }).start();

    }

    private void testShowPicturesInOrder(final List<Bitmap> picturesFromVideo) {

        new SecondCountDownTimer(picturesFromVideo.size(), 1, CountDownType.SECONDS, new ICountDownEvents() {
            private int index = 0;
            @Override
            public void onTick(Integer secondsRemaining) {
                FrameLayout layout = (FrameLayout)findViewById(R.id.camera_preview);
                layout.removeAllViews();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageBitmap(picturesFromVideo.get(index));
                layout.addView(imageView);

                index++;
            }

            @Override
            public void onFinish() {

            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            // this device has a front camera
            return true;
        } else {
            // no front camera on this device
            return false;
        }
    }

    private void startCamera(){
        if (checkCameraHardware(getApplicationContext())){
            camera = getCameraInstance();
            if (camera != null){
                cameraPreview = new CameraPreview(this, camera);
                camera.setPreviewCallback(previewCallback);
                FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
                preview.addView(cameraPreview);
            }else{
                System.out.println("COULD NOT GET CAMERA");
            }

        }else{
            System.out.println("WE DONT HAVE CAMERA");
        }
    }

    /** A safe way to get an instance of the Camera object. */
    /**should change to camer2 later, with @module and interface. First make it work*/
    @SuppressWarnings("deprecation")
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            for (int cameraId = 0; cameraId < cameraCount; cameraId++){
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    try{
                        c = Camera.open(cameraId); // attempt to get a Camera instance
                    }catch(RuntimeException e){
                        e.printStackTrace();
                    }

                }
            }
        }
        catch (Exception e){
            System.out.println("FRONT CAMERA NOT AVAILABLE");
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCamera(){
        if (cameraPreview!= null){
            cameraPreview.releaseCamera();
        }

        if (camera != null){
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            //camera.startPreview();
            if (analyzeRunning){
                videoAnalyzer.addFrame(new VideoFrame(data));
            }


        }

    };


}
