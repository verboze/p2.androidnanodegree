package com.vbz.spotifystreamer.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vbz.spotifystreamer.data.SpotifyDataStore;
import com.vbz.spotifystreamer.utils.MockData;

import kaaes.spotify.webapi.android.models.Track;

public class MediaPlayerService extends IntentService {
    private static final String LOG_TAG = MediaPlayerService.class.getSimpleName();
    public static final String ACTION_PLAY = "com.vbz.spotifystreamer.service.action.PLAY";
    public static final String ACTION_PAUSE = "com.vbz.spotifystreamer.service.action.PAUSE";
    public static final String ACTION_STOP = "com.vbz.spotifystreamer.service.action.STOP";
    public static final String ACTION_PREV = "com.vbz.spotifystreamer.service.action.PREV";
    public static final String ACTION_NEXT = "com.vbz.spotifystreamer.service.action.NEXT";

    private static final String EXTRA_ARTISTID = "com.vbz.spotifystreamer.service.extra.ARTISTID";
    private static final String EXTRA_TRACKID = "com.vbz.spotifystreamer.service.extra.TRACKID";

    private final MockData mockdata = new MockData();
    private SQLiteDatabase mDb;

    public static void startAction(Context context, String action, String artistid, String trackid) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction(action);
        intent.putExtra(EXTRA_ARTISTID, artistid);
        intent.putExtra(EXTRA_TRACKID, trackid);
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
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String artistid = intent.getStringExtra(EXTRA_ARTISTID);
            final String trackid = intent.getStringExtra(EXTRA_TRACKID);
            handleAction(action, artistid, trackid);
        }
    }

    private void handleAction(String action, String artistid, String trackid) {
        // TODO: read database, find all tracks by artist, find trackid (idx) in cursor, cursor.move(idx)
        switch(action) {
            case ACTION_PLAY:  play(artistid, trackid);  break;
            case ACTION_PAUSE: pause(artistid, trackid); break;
            case ACTION_STOP:  stop(artistid, trackid);  break;
            case ACTION_PREV:  prev(artistid, trackid);  break;
            case ACTION_NEXT:  next(artistid, trackid);  break;
            default:           Log.d(LOG_TAG, "invalid action: " + action); break;
        }
    }

    private void play(String artistid, String trackid) {
        String playerdata = " ARTIST: " + artistid + " TRACK: " + trackid;
        Log.d(LOG_TAG, "Action: "+ ACTION_PLAY + playerdata);

    }

    private void pause(String artistid, String trackid) {
        String playerdata = " ARTIST: " + artistid + " TRACK: " + trackid;
        Log.d(LOG_TAG, "Action: "+ ACTION_PAUSE + playerdata);

    }

    private void stop(String artistid, String trackid) {
        String playerdata = " ARTIST: " + artistid + " TRACK: " + trackid;
        Log.d(LOG_TAG, "Action: "+ ACTION_STOP + playerdata);

    }

    private void prev(String artistid, String trackid) {
        String playerdata = " ARTIST: " + artistid + " TRACK: " + trackid;
        Log.d(LOG_TAG, "Action: "+ ACTION_PREV + playerdata);

    }

    private void next(String artistid, String trackid) {
        String playerdata = " ARTIST: " + artistid + " TRACK: " + trackid;
        Log.d(LOG_TAG, "Action: "+ ACTION_NEXT + playerdata);

    }
}
