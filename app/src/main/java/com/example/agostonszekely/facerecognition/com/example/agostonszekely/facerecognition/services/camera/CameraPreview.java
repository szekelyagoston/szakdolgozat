package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.camera;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by agoston.szekely on 2016.10.20..
 */

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    private Camera camera;

    private static final int DISPLAY_ORIENTATION = 90;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        //getting ths surfaceview holder
        holder = getHolder();
        //set this callback to the callback
        holder.addCallback(this);

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(DISPLAY_ORIENTATION);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(DISPLAY_ORIENTATION);
            camera.startPreview();
        } catch (Exception e){

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void releaseCamera(){
        this.camera = null;
    }
}
