package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.facepp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gusztafszon on 2017-01-22.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacePPResponse {
    @JsonProperty("faces")
    private List<FacePPFaces> faces = new ArrayList<>();

    public List<FacePPFaces> getFaces() {
        return faces;
    }

    public void setFaces(List<FacePPFaces> faces) {
        this.faces = faces;
    }
}
