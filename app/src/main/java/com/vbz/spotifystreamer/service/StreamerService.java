package com.vbz.spotifystreamer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class StreamerService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String LOG_TAG = StreamerService.class.getSimpleName();

    public static final String ACTION_PLAY = "com.vbz.spotifystreamer.service.action.PLAY";
    public static final String ACTION_PAUSE = "com.vbz.spotifystreamer.service.action.PAUSE";
    public static final String ACTION_STOP = "com.vbz.spotifystreamer.service.action.STOP";

    private final IBinder mBinder = new StreamerBinder();
    private MediaPlayer mMediaPlayer = null;
    private boolean mPaused = false;

    /** iBinder interface to access this service */
    public class StreamerBinder extends Binder {
        public StreamerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StreamerService.this;
        }
    }

    public StreamerService() {
        super();
    }

    public void play(String trackurl) {
        if(mPaused) {
            // if player is in a paused state, resume
            Log.d(LOG_TAG, "Action: resuming: " + ACTION_PLAY + " URL: " + trackurl);
            mPaused = false;
            mMediaPlayer.start();
            return;
        }

        try {
            // this is request for a new track, prepare the player and play
            Log.d(LOG_TAG, "Action: starting: " + ACTION_PLAY + " URL: " + trackurl);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(trackurl);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "unable to play track: " + trackurl);
        }
    }

    public void pause() {
        Log.d(LOG_TAG, "Action: " + ACTION_PAUSE);
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPaused = true;
        }
    }

    public void stop() {
        Log.d(LOG_TAG, "Action: "+ ACTION_STOP);
        mMediaPlayer.stop();
    }

    public void seekTo(int percent) {
        // we only allow seeking when media is playing
        if(mPaused || mMediaPlayer.isPlaying()) {
            int location = mMediaPlayer.getDuration() * percent / 100;
            Log.d(LOG_TAG, "DURATION: " + mMediaPlayer.getDuration() + ", SEEKTO: " + percent + "%, LOCATION: " + location);
            mMediaPlayer.seekTo(location);
        }
    }

    public long getEllapsedTime() {
        return mMediaPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setOnCompletionListener(??); // TODO: define completion listener (must be trackfragment?!?)
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "stopping service...");
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // return instance of binder held by this service
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "media player error: " + what);
        return false;
    }

}
