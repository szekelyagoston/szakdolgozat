package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

public class FacePPFaceRectangle {
    @JsonProperty("width")
    private int width;
    @JsonProperty("top")
    private int top;
    @JsonProperty("left")
    private int left;
    @JsonProperty("height")
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
