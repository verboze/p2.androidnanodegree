package com.vbz.spotifystreamer.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vbz.spotifystreamer.data.SpotifyDataStore;
import com.vbz.spotifystreamer.utils.MockData;

public class MediaPlayerService extends IntentService {
    private static final String LOG_TAG = MediaPlayerService.class.getSimpleName();
    private static final String EXTRA_TRACkURL = "com.vbz.spotifystreamer.service.extra.TRACKURL";
    public static final String ACTION_PLAY = "com.vbz.spotifystreamer.service.action.PLAY";
    public static final String ACTION_PAUSE = "com.vbz.spotifystreamer.service.action.PAUSE";
    public static final String ACTION_STOP = "com.vbz.spotifystreamer.service.action.STOP";

    private final MockData mockdata = new MockData();
    private SQLiteDatabase mDb;
    private String mCurrTrack;

    public static void startAction(Context context, String action, String trackurl) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction(action);
        intent.putExtra(EXTRA_TRACkURL, trackurl);
        context.startService(intent);
    }

    public MediaPlayerService() {
        super("MediaPlayerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mockdata.create(this);
        mDb = new SpotifyDataStore(this).getReadableDatabase();
        mCurrTrack = null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            mCurrTrack = intent.getStringExtra(EXTRA_TRACkURL);
            handleAction(action);
        }
    }

    private void handleAction(String action) {
        switch(action) {
            case ACTION_PLAY:  play();  break;
            case ACTION_PAUSE: pause(); break;
            case ACTION_STOP:  stop();  break;
            default:           Log.d(LOG_TAG, "invalid action: " + action); break;
        }
    }

    private void play() {
        Log.d(LOG_TAG, "Action: "+ ACTION_PLAY + " URL: " + mCurrTrack);

    }

    private void pause() {
        Log.d(LOG_TAG, "Action: "+ ACTION_PAUSE + " URL: " + mCurrTrack);
    }

    private void stop() {
        Log.d(LOG_TAG, "Action: "+ ACTION_STOP);

    }
}
