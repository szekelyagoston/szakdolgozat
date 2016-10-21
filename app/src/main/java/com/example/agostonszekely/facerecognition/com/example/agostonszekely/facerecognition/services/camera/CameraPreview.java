package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.camera;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.20..
 */

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    private Camera camera;

    private static final int DISPLAY_ORIENTATION = 90;
    private int videoHeight = 800;
    private int videoWidth = 600;

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
            ViewGroup.LayoutParams params = this.getLayoutParams();
            List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();

            holder.getSurfaceFrame().width();
            holder.getSurfaceFrame().height();


            //finding the greatest height and width which still can fit in surface.
            int currentMaxHeight = 0;
            int maxheight = holder.getSurfaceFrame().width();
            int maxwidth = holder.getSurfaceFrame().height();
            //found a fitting frame
            Boolean l = false;
            for(Camera.Size size : sizes){
                //finding the greatest height which is smaller than maxheight
                if (size.width <= maxheight && size.height <= maxwidth && currentMaxHeight <= size.width){
                    currentMaxHeight = maxwidth;
                    videoWidth = size.height;
                    videoHeight = size.width;
                }
            }

            params.height = videoHeight;
            params.width = videoWidth;


            camera.getParameters().setPictureSize(videoWidth, videoHeight);
            this.setLayoutParams(params);
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
