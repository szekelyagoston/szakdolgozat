package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle;

/**
 * Created by agoston.szekely on 2016.10.17..
 */

public class FaceRectangle {
    private int left;

    private int top;

    private int right;

    private int bottom;

    public FaceRectangle(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public int getFaceRectangleValues(FaceRectangleEnum pos){
        switch(pos){
            case LEFT: {
                return this.left;
            }
            case TOP: {
                return this.top;
            }
            case RIGHT: {
                return this.right;
            }
            case BOTTOM: {
                return this.bottom;
            }
            default : {
                return 0;
            }
        }
    }
}
