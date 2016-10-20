package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.hardware.camera2.*;
import android.widget.FrameLayout;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.NavigationDrawerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.camera.CameraPreview;

/**
 * Created by agoston.szekely on 2016.10.20..
 */

@SuppressWarnings("deprecation")
public class VideoAnalyzerActivity extends NavigationDrawerActivity {
    private final int MENUPOSITION = 3;
    private Camera camera;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.camera_preview);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);

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
}
