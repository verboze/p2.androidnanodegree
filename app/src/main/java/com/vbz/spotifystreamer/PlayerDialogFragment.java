package com.vbz.spotifystreamer;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vbz.spotifystreamer.data.SpotifyCacheReader;
import com.vbz.spotifystreamer.service.StreamerService;
import com.vbz.spotifystreamer.service.StreamerService.StreamerBinder;

public class PlayerDialogFragment extends DialogFragment {
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

    private String _mArtistName;
    private String _mAlbumName;
    private String _mTrackName;

    private String mArtistId;
    private String mTrackId;
    private String mCurrTrack;

    private TextView mTvArtistName;
    private TextView mTvTrackTitle;
    private TextView mTvAlbumTitle;

    boolean mBound = false;
    StreamerService mService;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d(LOG_TAG_APP, "serviceConnected binding service...");
            StreamerBinder binder = (StreamerBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG_APP, "disconnecting service...");
            mBound = false;
        }
    };

    private String getPrevTrack() {
        // TODO: read from cursor
        return "https://p.scdn.co/mp3-preview/f7bf16ca988662cb1181f6b9a968992f98187b9c";
    }

    private String getNextTrack() {
        // TODO: read from cursor
        return "https://p.scdn.co/mp3-preview/dd7136085ecdaa8492a805b1b86814bc572252eb";
    }

    public PlayerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _mArtistName = getArguments().getString(ARG_ARTIST);
            _mAlbumName = getArguments().getString(ARG_ALBUM);
            _mTrackName = getArguments().getString(ARG_TRACK);
            mArtistId = getArguments().getString(ARG_ARTISTID);
            mTrackId = getArguments().getString(ARG_TRACKID);
            mCurrTrack = getArguments().getString(ARG_TRACKURL);
            Log.d(LOG_TAG_APP, "artist: " + _mArtistName + ", track: " + _mTrackName
                  + ", album: " + _mAlbumName + ", artistid: " + mArtistId + ", trackid: " + mTrackId);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View playerView = inflater.inflate(R.layout.fragment_player, null);
        mTvArtistName = ((TextView) playerView.findViewById(R.id.plyrArtistName));
        mTvTrackTitle = ((TextView) playerView.findViewById(R.id.plyrTrackTitle));
        mTvAlbumTitle = ((TextView) playerView.findViewById(R.id.plyrAlbumTitle));
        mTvArtistName.setText(_mArtistName);
        mTvTrackTitle.setText(_mTrackName);
        mTvTrackTitle.setText(_mAlbumName);

        // define listeners for the player buttons
        ((ToggleButton) playerView.findViewById(R.id.btnPayPause))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!mBound) {
                            Log.d(LOG_TAG_APP, "service not bound!!");
                            return;
                        }
                        if (isChecked) {
                            mService.play(mCurrTrack);
                        } else {
                            mService.pause();
                        }
                    }
                });
        playerView.findViewById(R.id.btnPayPause).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO: this background cannot be static
                playerView.findViewById(R.id.btnPayPause).setBackgroundResource(R.drawable.ic_play_circle_filled_black_48dp);
                if(mBound) { mService.stop(); }
                else { Log.d(LOG_TAG_APP, "service not bound!!"); }
                return true;
            }
        });
        playerView.findViewById(R.id.btnPrev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
                Bundle trackdata = ((TrackActivity) getActivity()).getPrevTrack();
                if(trackdata != null) {
                    mCurrTrack = trackdata.getString(ARG_TRACKURL);
                    mTvTrackTitle.setText(trackdata.getString(ARG_TRACK));
                    mTvAlbumTitle.setText(trackdata.getString(ARG_ALBUM));
                    mService.play(mCurrTrack);
                }
            }
        });
        playerView.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBound) { Log.d(LOG_TAG_APP, "service not bound!!"); return; }
                Bundle trackdata = ((TrackActivity) getActivity()).getNextTrack();
                if(trackdata != null) {
                    mCurrTrack = trackdata.getString(ARG_TRACKURL);
                    mTvTrackTitle.setText(trackdata.getString(ARG_TRACK));
                    mTvAlbumTitle.setText(trackdata.getString(ARG_ALBUM));
                    mService.play(mCurrTrack);
                }
            }
        });
        // TODO: IMPORTANT! implement seek bar
        // TODO: IMPORTANT! implement elapsed time

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
