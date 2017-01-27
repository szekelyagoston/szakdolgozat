package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.MicrosoftProjectOxfordFaceCapturingActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.pictures.PictureAnalyzerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.FacePPVIdeoAnalyzerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.GMOVIdeoAnalyzerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.MPOVideoAnalyzerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.VideoAnalyzerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera.videos.VideoAnalyzerModules;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.picture.FacePPActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.picture.MicrosoftProjectOxfordActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.picture.MobileVisionActivity;

/**
 * Created by agoston.szekely on 2016.10.18..
 */

public class NavigationDrawerActivity extends AppCompatActivity {

    protected String[] titles;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titles = getResources().getStringArray(R.array.drawer_titles);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titles));
        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch(position){
            case 0 : {
                Intent a = new Intent(this, MicrosoftProjectOxfordActivity.class);
                startActivity(a);
                break;
            }
            case 1 : {
                Intent a = new Intent(this, MobileVisionActivity.class);
                startActivity(a);
                break;
            }
            case 2 : {
                Intent a = new Intent(this, FacePPActivity.class);
                startActivity(a);
                break;
            }
            case 3 : {
                Intent a = new Intent(this, MicrosoftProjectOxfordFaceCapturingActivity.class);
                startActivity(a);
                break;
            }
            case 4 : {
                Intent a = new Intent(this, PictureAnalyzerActivity.class);
                startActivity(a);
                break;
            }
            case 5 : {
                Intent a = new Intent(this, MPOVideoAnalyzerActivity.class);
                a.putExtra("Module", VideoAnalyzerModules.MICROSOFT_PROJECT_OXFORD);
                startActivity(a);
                break;
            }
            case 6 : {
                Intent a = new Intent(this, GMOVIdeoAnalyzerActivity.class);
                a.putExtra("Module", VideoAnalyzerModules.GOOGLE_MOBILE_VISION);
                startActivity(a);
                break;
            }
            case 7 : {
                Intent a = new Intent(this, FacePPVIdeoAnalyzerActivity.class);
                a.putExtra("Module", VideoAnalyzerModules.FACE_PP);
                startActivity(a);
                break;
            }
        }

        // Highlight the selected item, update the title, and close the drawer
        /*drawerList.setItemChecked(position, true);
        setTitle(titles[position]);
        drawerLayout.closeDrawer(drawerList);*/
    }
}
