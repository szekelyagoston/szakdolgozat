package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

public class FacePPFaces {

    @JsonProperty("face_rectangle")
    private FacePPFaceRectangle faceRectangle;

    @JsonProperty("attributes")
    private FacePPAttributes faceAttributes;

    public FacePPFaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FacePPFaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public FacePPAttributes getFaceAttributes() {
        return faceAttributes;
    }

    public void setFaceAttributes(FacePPAttributes faceAttributes) {
        this.faceAttributes = faceAttributes;
    }
}
