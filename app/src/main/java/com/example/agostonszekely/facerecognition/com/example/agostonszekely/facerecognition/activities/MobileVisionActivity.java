package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;

import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public class MobileVisionActivity extends NavigationDrawerActivity{

    private final int PICK_IMAGE = 1;
    private final int MENUPOSITION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_mobile_vision);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);


        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_GET_CONTENT);
                callIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(callIntent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

}
