package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.challenge.utils;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public enum ChallengeTypes {
    TURN_HEAD_RIGHT(+20d), TURN_HEAD_LEFT(-20d);

    private Double from;

    ChallengeTypes(Double from){
        this.from = from;
    }

    public Double getFrom() {
        return from;
    }

    public String getText(){
        switch(this){
            case TURN_HEAD_RIGHT : {
                return "Please turn your head right";
            }
            case TURN_HEAD_LEFT : {
                return "Please turn your head left";
            }
        }
        return "Unknown challenge, there is a problem here";
    }
}
