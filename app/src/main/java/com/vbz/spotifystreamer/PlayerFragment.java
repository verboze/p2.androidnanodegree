package com.vbz.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayerFragment extends Fragment {
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

    public PlayerFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: receive current track to player on load?
        View playerView = inflater.inflate(R.layout.fragment_player, container, false);
        TextView artistlabel = (TextView) playerView.findViewById(R.id.plyrArtistName);
        TextView tracklabel  = (TextView) playerView.findViewById(R.id.plyrTrackTitle);
        TextView albumlabel  = (TextView) playerView.findViewById(R.id.plyrAlbumTitle);
        artistlabel.setText(mArtistName);
        tracklabel.setText(mTrackName);
        albumlabel.setText(mAlbumName);
        return playerView;
    }

    /*
    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    */

}
