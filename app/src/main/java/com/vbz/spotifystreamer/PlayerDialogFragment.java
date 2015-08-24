package com.vbz.spotifystreamer;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vbz.spotifystreamer.service.MediaPlayerService;

public class PlayerDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final String FRAGMENT_NAME = "SPOTPLAYER";
    private static final String LOG_TAG_APP  = "SPOTSTREAMER";
    private static final String LOG_TAG_FRAG = "SPOTPLAYER";

    private static final String ARG_PARAM1 = "artist";
    private static final String ARG_PARAM2 = "album";
    private static final String ARG_PARAM3 = "track";

    private String mArtistName;
    private String mAlbumName;
    private String mTrackName;

    // TODO: handle fragment lifecycle (especially rotation cases)
    // TODO: implement onClickListeners for player buttons

    public PlayerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtistName = getArguments().getString(ARG_PARAM1);
            mAlbumName = getArguments().getString(ARG_PARAM2);
            mTrackName = getArguments().getString(ARG_PARAM3);
            Log.d(LOG_TAG_APP, "artist: " + mArtistName + ", track: " + mTrackName + ", album: " + mAlbumName);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO: receive current track to player on load?
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View playerView = inflater.inflate(R.layout.fragment_player, null);
        ((TextView) playerView.findViewById(R.id.plyrArtistName)).setText(mArtistName);
        ((TextView) playerView.findViewById(R.id.plyrTrackTitle)).setText(mTrackName);
        ((TextView) playerView.findViewById(R.id.plyrAlbumTitle)).setText(mAlbumName);

        // define actions for the player buttons
        ((ToggleButton) playerView.findViewById(R.id.btnPayPause))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO: retrieve idx. use a member var to track cursor position?
                    if (isChecked) {
                        MediaPlayerService.startAction(getActivity(),
                                MediaPlayerService.ACTION_PAUSE, mTrackName);
                    } else {
                        MediaPlayerService.startAction(getActivity(),
                                MediaPlayerService.ACTION_PLAY, mTrackName);
                    }
                }
        });
        playerView.findViewById(R.id.btnPrev).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MediaPlayerService.startAction(getActivity(),
                        MediaPlayerService.ACTION_PLAY, " -1 " + mTrackName);
            }
        });
        playerView.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                MediaPlayerService.startAction(getActivity(),
                        MediaPlayerService.ACTION_PLAY, " +1 " + mTrackName);
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
    }
