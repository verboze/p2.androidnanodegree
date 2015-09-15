package com.vbz.spotifystreamer;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements PlayerDialogFragment.onTrackChangedListener, ArtistViewFragment.SelectionCallback {
    private static final String LOG_TAG_APP = "SPOTSTREAMER";
    private boolean mTwoPanes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        ArtistViewFragment avf = (ArtistViewFragment) fm.findFragmentByTag(ArtistViewFragment.FRAGMENT_NAME);
        TrackViewFragment tvf = (TrackViewFragment) fm.findFragmentByTag(TrackViewFragment.FRAGMENT_NAME);

        // if artist list was not previously loaded, load it now
        if(avf == null) {
            avf = new ArtistViewFragment();
            fm.beginTransaction()
                    .add(R.id.main_list_container, avf, ArtistViewFragment.FRAGMENT_NAME)
                    .commit();
        }

        if(findViewById(R.id.tracks_list_container) != null) {
            Log.d(LOG_TAG_APP, "instantiating track list frag");
            mTwoPanes = true;
            if (savedInstanceState == null) {
                if(tvf == null) { tvf = new TrackViewFragment(); }
                fm.beginTransaction()
                    .add(R.id.tracks_list_container, tvf, TrackViewFragment.FRAGMENT_NAME)
                    .commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onItemSelected(Bundle data) {
        // handle list item click, dispatch track detail to either
        // a new activity or to an adjacent fragment
        // TODO: IMPORTANT! fix 'rotate from landscape to normal' crashes app No view found for id 0x7f0c0051
        if (mTwoPanes) {
            TrackViewFragment tvf = new TrackViewFragment();
            tvf.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tracks_list_container, tvf, TrackViewFragment.FRAGMENT_NAME)
                    .commit();
        } else {
            // TODO: why is the loaded fragment not taking full width of parent?!?
            TrackViewFragment tvf = new TrackViewFragment();
            tvf.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_list_container, tvf, TrackViewFragment.FRAGMENT_NAME)
                    .addToBackStack(TrackViewFragment.FRAGMENT_NAME)
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTrackData() {

    }

    public void RetrieveTrackData() {

    }

    public void clearTrackList() {
        FragmentManager fm = getSupportFragmentManager();
        TrackViewFragment tvf = (TrackViewFragment) fm.findFragmentByTag(TrackViewFragment.FRAGMENT_NAME);
        if (tvf != null) {
            fm.beginTransaction().remove(tvf).commit();
        }

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

    public void saveState() {
        // TODO: I want to save the current state of the player. maybe better handled in onSaveInstance?
    }

    public void showUpButton() {
        try { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        catch(NullPointerException npe) { Log.e(LOG_TAG_APP, "umm, why?\n" + npe.toString()); }
    }

    public void hideUpButton() {
        try { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }
        catch(NullPointerException npe) { Log.e(LOG_TAG_APP, "umm, why?\n" + npe.toString()); }
    }
}

// TODO: NICE TO HAVES:
//       display message in listview on no data found
//       show loader when data is being fetched
