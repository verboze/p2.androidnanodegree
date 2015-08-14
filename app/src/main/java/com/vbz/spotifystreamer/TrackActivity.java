package com.vbz.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class TrackActivity extends AppCompatActivity {
    private static final String LOG_TAG_APP = "SPOTSTREAMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.maincontainer, new TrackViewFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dismissPlayer(View v) {
        Log.d(LOG_TAG_APP, "popping player view: " + PlayerFragment.class.getName());
        // TODO: fix. howcome it no work?!?
//        getFragmentManager().popBackStack(PlayerFragment.class.getName(), 0);
        getFragmentManager().popBackStack();
    }

    /*
    FrameLayout player = (FrameLayout) findViewById(R.id.music_player);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (player.isPressed()){
            getFragmentManager().popBackStack();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    player.setOnTouchListener(new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            if (application.getDbManager().getUser().key.equals("-1")){
                hideLoginFragment();
                loginButton.setVisibility(View.VISIBLE);
                exitButton.setVisibility(View.INVISIBLE);}
            return false;
        }
    });
    */
}
