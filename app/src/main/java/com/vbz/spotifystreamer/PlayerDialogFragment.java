package com.vbz.spotifystreamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vbz.spotifystreamer.service.StreamerService;
import com.vbz.spotifystreamer.service.StreamerService.StreamerBinder;

public class PlayerDialogFragment extends DialogFragment {
    public interface onTrackChangedListener {
        // containing activity must implement this interface to allow
        // player fragment to skip tracks back and forth
        Bundle getPrevTrack();
        Bundle getNextTrack();
    }

    // TODO: handle fragment lifecycle (especially rotation cases)
    public static final String FRAGMENT_NAME = "SPOTPLAYER";
    private static final String LOG_TAG_APP  = "SPOTSTREAMER";
    private static final String LOG_TAG_FRAG = "SPOTPLAYER";

    private static final String ARG_ARTIST = "artist";
    private static final String ARG_ALBUM = "album";
    private static final String ARG_TRACK = "track";
    private static final String ARG_ARTISTID = "artistid";
    private static final String ARG_TRACKID = "trackid";
    private static final String ARG_TRACKURL = "trackurl";

    private PlayerUtils mPlayerUtils = new PlayerUtils();
    private Handler mTimeTracker = new Handler();

    // current track info
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
//    private int seekForwardTime = 5000; // in millisecs
//    private int seekBackwardTime = 5000; // in millisecs
    boolean mBound = false;
    StreamerService mMusicPlayerService;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d(LOG_TAG_APP, "serviceConnected binding service...");
            StreamerBinder binder = (StreamerBinder) service;
            mMusicPlayerService = binder.getService();
            mBound = true;
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

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long elapsedmillisecs = mMusicPlayerService.getEllapsedTime();
            long duration =  mMusicPlayerService.getDuration();
            String ellapsedsecs = mPlayerUtils.timeToString(elapsedmillisecs);
            int progress = mPlayerUtils.getCurrentPercentage(elapsedmillisecs, duration);
            Log.d(LOG_TAG_APP, "trying to update UI with ellapsed: "+ellapsedsecs+" progress: "+progress);

            // update UI elements
            mTvElapsedTime.setText(ellapsedsecs);
            mSbSeekBar.setProgress(progress);

            // post another update a second from now
            mTimeTracker.postDelayed(this, 500);
        }
    };

    private void startTimer() {
        Log.d(LOG_TAG_APP, "updating timer...");
        mTimeTracker.postDelayed(updateTimer, 500);
    }

    private void stopTimer() {
        Log.d(LOG_TAG_APP, "stopping timer...");
        mTimeTracker.removeCallbacks(updateTimer);
        // TODO: must be called when track finishes playing (OnCompletionListener)
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog, retrieve UI views
        // Pass null as the parent view because its going in the dialog layout
        final View playerView = inflater.inflate(R.layout.fragment_player, null);
        mImgAlbumArt   = (ImageView) playerView.findViewById(R.id.imgAlbumArt);
        mTvArtistName  = (TextView) playerView.findViewById(R.id.plyrArtistName);
        mTvTrackTitle  = (TextView) playerView.findViewById(R.id.plyrTrackTitle);
        mTvAlbumTitle  = (TextView) playerView.findViewById(R.id.plyrAlbumTitle);
        mTvElapsedTime = (TextView) playerView.findViewById(R.id.elapsedTime);
        mTvDuration    = (TextView) playerView.findViewById(R.id.duration);
        mSbSeekBar     = (SeekBar) playerView.findViewById(R.id.musicSeekbar);
        mBtnPlayPause  = (ToggleButton) playerView.findViewById(R.id.btnPayPause);
        mBtnPrev       = (ImageButton) playerView.findViewById(R.id.btnPrev);
        mBtnNext       = (ImageButton) playerView.findViewById(R.id.btnNext);


        // player control configuration
        mBtnPlayPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mBound) {
                    Log.d(LOG_TAG_APP, "service not bound!!");
                    return;
                }
                if (isChecked) {
                    Bundle trackdata = ((TrackActivity) getActivity()).getCurrTrack();
                    if(trackdata != null) {
                        mCurrTrack = trackdata.getString(ARG_TRACKURL);
                        mTvArtistName.setText(trackdata.getString(ARG_ARTIST));
                        mTvTrackTitle.setText(trackdata.getString(ARG_TRACK));
                        mTvAlbumTitle.setText(trackdata.getString(ARG_ALBUM));
                        mMusicPlayerService.play(mCurrTrack);
                        startTimer();
                    }
                } else {
                    stopTimer();
                    mMusicPlayerService.pause();
                }
            }
        });
        mBtnPlayPause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO: this background cannot be static
                mBtnPlayPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                if(mBound) {
                    stopTimer();
                    mMusicPlayerService.stop();
                } else {
                    Log.d(LOG_TAG_APP, "service not bound!!");
                }
                return true;
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
                Bundle trackdata = ((TrackActivity) getActivity()).getPrevTrack();
                if(trackdata != null) {
                    mCurrTrack = trackdata.getString(ARG_TRACKURL);
                    mTvArtistName.setText(trackdata.getString(ARG_ARTIST));
                    mTvTrackTitle.setText(trackdata.getString(ARG_TRACK));
                    mTvAlbumTitle.setText(trackdata.getString(ARG_ALBUM));
                    mMusicPlayerService.play(mCurrTrack);
                }
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
                Bundle trackdata = ((TrackActivity) getActivity()).getNextTrack();
                if(trackdata != null) {
                    mCurrTrack = trackdata.getString(ARG_TRACKURL);
                    mTvArtistName.setText(trackdata.getString(ARG_ARTIST));
                    mTvTrackTitle.setText(trackdata.getString(ARG_TRACK));
                    mTvAlbumTitle.setText(trackdata.getString(ARG_ALBUM));
                    mMusicPlayerService.play(mCurrTrack);
                }
            }
        });
        mSbSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                stopTimer();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicPlayerService.seekTo(progressChanged);
                startTimer();
            }
        });

        //build player UI
        builder.setView(playerView);
        Dialog popupPlayer = builder.create();

        // align modal player window to bottom of screen
        Window win = popupPlayer.getWindow();
        WindowManager.LayoutParams wlp = win.getAttributes();
        wlp.gravity = Gravity.BOTTOM;

        return popupPlayer;
    }

    @Override
    public void onStart() {
        super.onStart();
        // bind streamer service to enable playback on track click
        Log.d(LOG_TAG_APP, "starting player. should be binding here...");
        Intent intent = new Intent(getActivity(), StreamerService.class);
        getActivity().bindService(intent, mConnection, getActivity().BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG_APP, "destroying player");
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }
}
