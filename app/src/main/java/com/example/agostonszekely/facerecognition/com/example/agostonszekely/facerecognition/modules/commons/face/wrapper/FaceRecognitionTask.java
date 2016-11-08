package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ágoston on 05/11/2016.
 */

public class FaceRecognitionTask {
    private Boolean finished;

    private List<FaceProperties> faces;

    public FaceRecognitionTask() {
        this.finished = false;
        this.faces = new ArrayList<>();
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<FaceProperties> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceProperties> faces) {
        this.faces = faces;
    }
}
