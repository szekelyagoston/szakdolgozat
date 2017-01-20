package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.os.Bundle;

/**
 * Created by agoston.szekely on 2017.01.11..
 */

public class GMOVIdeoAnalyzerActivity extends VideoAnalyzerActivity{
    private final int MENUPOSITION = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);
    }
}
