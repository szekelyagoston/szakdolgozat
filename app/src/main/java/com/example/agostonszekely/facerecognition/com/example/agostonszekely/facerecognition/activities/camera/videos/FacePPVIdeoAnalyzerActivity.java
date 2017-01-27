package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos;

import android.os.Bundle;

/**
 * Created by agoston.szekely on 2017.01.24..
 */

public class FacePPVIdeoAnalyzerActivity extends VideoAnalyzerActivity{
    private final int MENUPOSITION = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);
    }
}
