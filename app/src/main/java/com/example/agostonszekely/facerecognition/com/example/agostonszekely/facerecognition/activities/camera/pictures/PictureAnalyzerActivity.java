package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.pictures;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.NavigationDrawerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangleEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.camera.CameraPreview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.width;

/**
 * Created by agoston.szekely on 2016.10.20..
 */

@SuppressWarnings("deprecation")
public class PictureAnalyzerActivity extends NavigationDrawerActivity {
    private final int MENUPOSITION = 3;
    private Camera camera;
    private CameraPreview cameraPreview;

    private IFaceRecognition faceRecognition;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.camera_preview);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        camera.takePicture(null, null, pictureCallback);
                    }
                }
        );

        faceRecognition = new MicrosoftProjectOxford(new AsyncResponse<List<FaceProperties>>() {
            @Override
            public void processFinish(List<FaceProperties> faces) {

                ImageView imageView = (ImageView)findViewById(R.id.imageView1);
                imageView.setImageBitmap(drawFaceRectanglesOnBitmap(bitmap, faces));
                if (bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                    bitmap = null;
                }


                //ugly. should be placed above rectangles
                if (faces.size() >0){
                    TextView rollTextView = (TextView)findViewById(R.id.roll);
                    rollTextView.setText(faces.get(0).getFacePosition().getFacePosition().getReadableName() + " ( " + faces.get(0).getFacePosition().getYaw() + " )" ) ;
                }

            }
        });


        startCamera();
    }

    @Override
    protected void onPause(){
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

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Matrix rotationMatrix = null;
            try {
                rotationMatrix = OrientationHelper.rotate270();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapProducer.CreateBitmap(bitmap, rotationMatrix);
            bitmap = BitmapProducer.MirrorBitmap(bitmap);

            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            imageView.setImageBitmap(bitmap);

            try {
                detectAndFrame(bitmap);
            } catch (NoContextPresentException e) {
                e.printStackTrace();
            }
        }
    };

    private void detectAndFrame(final Bitmap imageBitmap) throws NoContextPresentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //compress quality -> if too high, method will be slow.
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 3, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        //TODO: HANDLING async stuff
        faceRecognition.getFaceRectangle(inputStream);

    }

    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, List<FaceProperties> faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 6;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {
            for (FaceProperties face : faces) {

                canvas.drawRect(
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.LEFT),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.TOP),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.RIGHT),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.BOTTOM),
                        paint);
            }
        }
        return bitmap;
    }
}
