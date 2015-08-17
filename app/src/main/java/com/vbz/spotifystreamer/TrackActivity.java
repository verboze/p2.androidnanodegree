package com.vbz.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TrackActivity extends AppCompatActivity {
    private static final String LOG_TAG_APP = "SPOTSTREAMER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            TrackViewFragment tvf = new TrackViewFragment();
            Bundle args = new Bundle();
            args.putString("artistname", intent.getStringExtra("artistname"));
            args.putString("artistid", intent.getStringExtra("artistid"));
            tvf.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tracks_fragment_container, tvf)
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
        getFragmentManager().popBackStack(PlayerFragment.FRAGMENT_NAME, 0);
    }
}
