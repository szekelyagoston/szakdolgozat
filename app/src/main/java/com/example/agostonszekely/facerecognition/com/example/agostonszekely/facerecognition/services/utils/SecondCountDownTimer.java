package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.services.utils;

import android.os.CountDownTimer;

/**
 * Created by agoston.szekely on 2016.10.21..
 */
//HACKED Beacouse CountDownTimer does not call ontTick when remaining millis is smaller than interval. *10 and / 10 is a hack to "solve" it. It really does not solve it, buit the difference will be 10 times smaller
public class SecondCountDownTimer{
    private int secondsRemaining;
    private int tick;
    private ICountDownEvents delegate;

    public SecondCountDownTimer(int seconds, int tick, ICountDownEvents delegate){
        this.secondsRemaining = seconds * 10;
        this.delegate = delegate;
        this.tick = tick;
    }

    public void start(){
        new CountDownTimer(secondsRemaining * 100, tick * 100) {
            public void onTick(long millisUntilFinished) {
                secondsRemaining = secondsRemaining - tick;
                if (secondsRemaining % 10 == 0){
                    delegate.onTick(secondsRemaining / 10);
                }
            }

            public void onFinish() {
                delegate.onFinish();
            }
        }.start();
    }
}
