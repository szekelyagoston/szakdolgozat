package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by agoston.szekely on 2016.10.14..
 */

public class BitmapProducer {
    public static final Bitmap CreateBitmap(Bitmap src, Matrix rotationMatrix){
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), rotationMatrix, true);
    }
}
