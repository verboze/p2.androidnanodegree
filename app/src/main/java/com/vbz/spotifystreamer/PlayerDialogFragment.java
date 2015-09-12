package com.vbz.spotifystreamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.vbz.spotifystreamer.service.StreamerService;
import com.vbz.spotifystreamer.service.StreamerService.StreamerBinder;
import com.vbz.spotifystreamer.utils.PlayerUtils;

public class PlayerDialogFragment extends DialogFragment
    implements MediaPlayer.OnCompletionListener {

    public interface onTrackChangedListener {
        // containing activity must implement this interface to allow
        // player fragment to navigate tracks
        Bundle getCurrTrack();
        Bundle getPrevTrack();
        Bundle getNextTrack();
    }

    public static final String FRAGMENT_NAME = "SPOTPLAYER";
    private static final String LOG_TAG_APP  = "SPOTSTREAMER";
    private static final String LOG_TAG_FRAG = "SPOTPLAYER";
    public static final int STARTTIMER = 10;
    public static final int STOPTIMER = 20;
    public static final int RESETELAPSED = 30;
    public static final int SETDURATION = 40;

    public Handler mPlaybackUIHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STARTTIMER:   if (! mMusicPlayerService.isPaused()) startElapsedTimer(); break;
                case STOPTIMER:    stopElapsedTimer(); break;
                case RESETELAPSED: resetElapsedTime(); break;
                case SETDURATION:  setDuration(); break;
                default: break;
            }
        }
    };

    private PlayerUtils mPlayerUtils = new PlayerUtils();
    private boolean mIsLargeScreen = false;
    private String mCurrTrack;

    // player controls and other UI elements
    private ImageView mImgAlbumArt;
    private ToggleButton mBtnPlayPause;
    private ImageButton mBtnPrev;
    private ImageButton mBtnNext;
    private SeekBar mSbSeekBar;
    private TextView mTvArtistName;
    private TextView mTvTrackTitle;
    private TextView mTvAlbumTitle;
    private TextView mTvElapsedTime;
    private TextView mTvDuration;

    // misc vars
    boolean mBound = false;
    long mDuration = 0;
    StreamerService mMusicPlayerService;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d(LOG_TAG_APP, "serviceConnected binding service...");
            StreamerBinder binder = (StreamerBinder) service;
            mMusicPlayerService = binder.getService();
            mMusicPlayerService.setHandler(mPlaybackUIHandler);
            mMusicPlayerService.setOnCompletionListener((MediaPlayer.OnCompletionListener) PlayerDialogFragment.this);
            mBound = true;

            Bundle trackdata = ((onTrackChangedListener) getActivity()).getCurrTrack();
            if (trackdata != null) {
                setTrackDetails(trackdata);
                mMusicPlayerService.play(mCurrTrack);
                mBtnPlayPause.setChecked(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG_APP, "disconnecting service...");
            mBound = false;
        }
    };

    public PlayerDialogFragment() {
        // Required empty public constructor
    }

    // Handler methods to update UI on music playback actions
    private Runnable updateTimer = new Runnable() {
        // TODO: fix jittery progress update
        @Override public void run() {
            // update UI elements
            long elapsedmillisecs = mMusicPlayerService.getEllapsedTime();
            int progress = mPlayerUtils.getCurrentPercentage(elapsedmillisecs, mDuration);
            mSbSeekBar.setProgress(progress);

            // post another update a second from now
            mPlaybackUIHandler.postDelayed(this, 500);
        }
    };

    private void startElapsedTimer() {
        Log.d(LOG_TAG_APP, "starting elapsedTimer...");
        mPlaybackUIHandler.post(updateTimer);
    }

    private void stopElapsedTimer() {
        Log.d(LOG_TAG_APP, "stopping elapsedTimer...");
        mPlaybackUIHandler.removeCallbacks(updateTimer);
    }

    private void resetElapsedTime() {
        mTvElapsedTime.setText("00:00");
//        mTvDuration.setText("00:00");
        mSbSeekBar.setProgress(0);
    }

    private void setElapsedTime(int percent, long totalDuration) {
        // TODO: fix jittery progress update
        long elapsed =  mPlayerUtils.getElapsedFromPercentage(percent, totalDuration);
        Log.d(LOG_TAG_APP, "percent:"+percent+", elapsed: "+elapsed+", total: "+totalDuration);
        mTvElapsedTime.setText(mPlayerUtils.timeToString(elapsed)); // cheating a little bit...

        /////////////////////////////////////////
//        long elapsedmillisecs = mMusicPlayerService.getEllapsedTime();
//        String ellapsedsecs = mPlayerUtils.timeToString(elapsedmillisecs);
//        mTvElapsedTime.setText(ellapsedsecs);
    }

    private void setDuration() {
        mDuration =  mMusicPlayerService.getDuration();
        mTvDuration.setText(mPlayerUtils.timeToString(mDuration));
    }

    private void setTrackDetails(Bundle trackdata) {
        mCurrTrack = trackdata.getString(TrackViewFragment.PARAM_TRACKURL);
        mTvArtistName.setText(trackdata.getString(TrackViewFragment.PARAM_ARTIST));
        mTvTrackTitle.setText(trackdata.getString(TrackViewFragment.PARAM_TRACK));
        mTvAlbumTitle.setText(trackdata.getString(TrackViewFragment.PARAM_ALBUM));
        Picasso.with(getActivity())
                .load(trackdata.getString(TrackViewFragment.PARAM_ALBUMART))
                .resize(300, 300)
                .into(mImgAlbumArt);
    }

    private void playPauseAction() {
        if (!mBound) {
            Log.d(LOG_TAG_APP, "service not bound!!");
            return;
        }
        if (mBtnPlayPause.isChecked()) {
            Bundle trackdata = ((onTrackChangedListener) getActivity()).getCurrTrack();
            if (trackdata != null) {
                setTrackDetails(trackdata);
                mMusicPlayerService.play(mCurrTrack);

            }
        } else {
            mMusicPlayerService.pause();
        }

    }

    private void prevAction() {
        if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
        Bundle trackdata = ((onTrackChangedListener) getActivity()).getPrevTrack();
        if(trackdata != null) {
            setTrackDetails(trackdata);
            resetElapsedTime();
            mMusicPlayerService.stop();
            mMusicPlayerService.play(mCurrTrack);
            mBtnPlayPause.setChecked(true);
        }

    }

    private void nextAction() {
        if (!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
        Bundle trackdata = ((onTrackChangedListener) getActivity()).getNextTrack();
        if (trackdata != null) {
            setTrackDetails(trackdata);
            resetElapsedTime();
            mMusicPlayerService.stop();
            mMusicPlayerService.play(mCurrTrack);
            mBtnPlayPause.setChecked(true);
        }

    }

    private void stopAction() {
        if (mBound) {
            resetElapsedTime();
            mBtnPlayPause.setChecked(false);
            mMusicPlayerService.stop();
        } else {
            Log.d(LOG_TAG_APP, "service not bound!!");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsLargeScreen = getResources().getBoolean(R.bool.largescreen);
        Log.d(LOG_TAG_APP, "ISLARGSCREEN: "+mIsLargeScreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate and set the layout for the dialog, retrieve UI elements
        int layout = 0;
        if (mIsLargeScreen) { Log.d(LOG_TAG_APP, "loading overlay player"); layout = R.layout.fragment_overlay_player; }
        else { Log.d(LOG_TAG_APP, "loading full player"); layout = R.layout.fragment_full_player; }
        final View playerView = inflater.inflate(layout, null);
        mImgAlbumArt = (ImageView) playerView.findViewById(R.id.imgAlbumArt);
        mTvArtistName = (TextView) playerView.findViewById(R.id.plyrArtistName);
        mTvTrackTitle = (TextView) playerView.findViewById(R.id.plyrTrackTitle);
        mTvAlbumTitle = (TextView) playerView.findViewById(R.id.plyrAlbumTitle);
        mTvElapsedTime = (TextView) playerView.findViewById(R.id.elapsedTime);
        mTvDuration = (TextView) playerView.findViewById(R.id.duration);
        mSbSeekBar = (SeekBar) playerView.findViewById(R.id.musicSeekbar);
        mBtnPlayPause = (ToggleButton) playerView.findViewById(R.id.btnPayPause);
        mBtnPrev = (ImageButton) playerView.findViewById(R.id.btnPrev);
        mBtnNext = (ImageButton) playerView.findViewById(R.id.btnNext);

        // player control configuration
        mBtnPlayPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playPauseAction();
            }
        });
        mBtnPlayPause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                stopAction();
                return true;
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevAction();
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAction();
            }
        });
        mSbSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                setElapsedTime(progressChanged, mDuration);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                stopElapsedTimer();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicPlayerService.seekTo(progressChanged);
            }
        });

        // if on a large screen, display dialog in modal fashion
        if(mIsLargeScreen) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(playerView);
            Dialog popupPlayer = builder.create();

            // align modal player window to bottom of screen
            Window win = popupPlayer.getWindow();
            WindowManager.LayoutParams wlp = win.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
        }

        return playerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // bind streamer service to enable playback on track click
        Log.d(LOG_TAG_APP, "binding player...");
        Intent intent = new Intent(getActivity(), StreamerService.class);
        getActivity().bindService(intent, mConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG_APP, "destroying player");
        stopElapsedTimer();
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(LOG_TAG_APP, "calling completion cb...");
        if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
        mBtnPlayPause.setChecked(false);
        stopElapsedTimer(); // kill timer for previous track
        Bundle trackdata = ((onTrackChangedListener) getActivity()).getNextTrack();
        if(trackdata != null) {
            setTrackDetails(trackdata);
            mMusicPlayerService.play(mCurrTrack);
            mBtnPlayPause.setChecked(true);
        }
    }
}
