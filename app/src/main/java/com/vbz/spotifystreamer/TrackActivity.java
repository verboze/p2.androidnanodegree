package com.vbz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TrackActivity extends AppCompatActivity
        implements PlayerDialogFragment.onTrackChangedListener {
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
                    .add(R.id.tracks_fragment_container, tvf, TrackViewFragment.FRAGMENT_NAME)
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

    public Bundle getCurrTrack() {
        TrackViewFragment tv =
                (TrackViewFragment) getSupportFragmentManager()
                        .findFragmentByTag(TrackViewFragment.FRAGMENT_NAME);
        if(tv != null) {
            Bundle res = tv.getTrackDataByOffset(0);
            if (res == null) Toast.makeText(this, "no tracks available to play", Toast.LENGTH_SHORT).show();
            return res;
        } else {
            Log.d(LOG_TAG_APP, "could not retrieve fragment: " + TrackViewFragment.FRAGMENT_NAME);
            return null;
        }
    }

    public Bundle getPrevTrack() {
        TrackViewFragment tv =
                (TrackViewFragment) getSupportFragmentManager()
                    .findFragmentByTag(TrackViewFragment.FRAGMENT_NAME);
        if(tv != null) {
            Bundle res = tv.getTrackDataByOffset(-1);
            if (res == null) Toast.makeText(this, "already at beginning of list", Toast.LENGTH_SHORT).show();
            return res;
        } else {
            Log.d(LOG_TAG_APP, "could not retrieve fragment: " + TrackViewFragment.FRAGMENT_NAME);
            return null;
        }
    }

    public Bundle getNextTrack() {
        TrackViewFragment tv =
                (TrackViewFragment) getSupportFragmentManager()
                        .findFragmentByTag(TrackViewFragment.FRAGMENT_NAME);
        if(tv != null) {
            Bundle res = tv.getTrackDataByOffset(+1);
            if (res == null) Toast.makeText(this, "already at end of list", Toast.LENGTH_SHORT).show();
            return res;
        } else {
            Log.d(LOG_TAG_APP, "could not retrieve fragment: " + TrackViewFragment.FRAGMENT_NAME);
            return null;
        }
    }
}
