package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services;

import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.position.FacePosition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.challenge.utils.ChallengeTypes;

import java.util.List;

/**
 * Created by agoston.szekely on 2016.11.04..
 */

public class ChallengeResult {
    private ChallengeTypes challengeType;
    private List<FacePosition> processedFaces;
    private Boolean isAccepted;

    public ChallengeResult(ChallengeTypes challengeType) {
        this.challengeType = challengeType;
        this.isAccepted = false;
    }

    public ChallengeTypes getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(ChallengeTypes challengeType) {
        this.challengeType = challengeType;
    }

    public List<FacePosition> getProcessedFaces() {
        return processedFaces;
    }

    public void setProcessedFaces(List<FacePosition> processedFaces) {
        this.processedFaces = processedFaces;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
