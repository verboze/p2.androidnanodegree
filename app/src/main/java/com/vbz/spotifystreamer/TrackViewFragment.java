package com.vbz.spotifystreamer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TrackViewFragment extends ListFragment {
    public static final String FRAGMENT_NAME = "TRACKLIST";
    private static final String LOG_TAG_APP = "SPOTSTREAMER";
    private static final String LOG_TAG_API = "SPOTAPI";
    public static final String PARAM_ARTIST = "artist";
    public static final String PARAM_ALBUM = "album";
    public static final String PARAM_TRACK = "track";
    public static final String PARAM_TRACKURL = "trackurl";
    public static final String PARAM_ARTISTID = "artistid";
    public static final String PARAM_TRACKID = "trackid";
    public static final String PARAM_ALBUMART = "albumart";

    private boolean mIsLargeScreen = false;
    private List<Track> datalist; // holds data retrieved from API
    private SpotifyService spotifysvc = new SpotifyApi().getService();
    private boolean was_data_fetched = false;
    private String mArtistName = null;
    private String mArtistId = null;
    private Cursor mCursor = null;
    private int mCurrPos = 0;

    public TrackViewFragment() {
        // required empty constructor
    }

    private void fetchTracks(String artistid) {
        Log.d(LOG_TAG_APP, "looking up tracks for " + artistid + "...");
        HashMap<String, Object> country = new HashMap<String, Object>();
        country.put("country", "us");
        spotifysvc.getArtistTopTrack(artistid, country, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                Log.d(LOG_TAG_API, "found tracks: " + tracks.tracks);
                setListdata(tracks.tracks);
                if (tracks.tracks.size() > 0) {
                    setListdata(tracks.tracks);
                } else {
                    Toast.makeText(getActivity(), "no top tracks found for artist ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG_API, "failed to retrieve artist top tracks:\n" + error.toString());
                Toast.makeText(getActivity(), "failed to retrieve tracks. Possible network issues?: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setListdata(List<Track> data) {
        datalist.clear();

        if (data.size() > 0) {
            for (Track i : data) {
                datalist.add(i);
            }
            setListAdapter(new TrackListViewAdapter(getActivity(), datalist));
        } else {
            Log.d(LOG_TAG_APP, "no data found");
        }
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
        setRetainInstance(true);
        if (savedinstanceSate == null) { datalist = new ArrayList<>(); }
        mIsLargeScreen = getResources().getBoolean(R.bool.largescreen);
        if(! mIsLargeScreen) { ((MainActivity)getActivity()).showUpButton(); }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(args == null) { return null; }

        mArtistName = args.getString("artistname");
        mArtistId = args.getString("artistid");

        View trackView = inflater.inflate(R.layout.fragment_tracks, container, false);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) { ab.setSubtitle(mArtistName); }
        if (!was_data_fetched) {
            fetchTracks(mArtistId);
            was_data_fetched = true;
        }

        return trackView;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        mCurrPos = position;
        Bundle data = getTrackDataByOffset(0);
        if(data != null) { showPlayer(data); }
    }

    public Bundle getTrackDataByOffset(int offset) {
        int newPos = mCurrPos + offset;
        if(0 <= newPos && newPos < datalist.size()) {
            mCurrPos = newPos;
            Track track = datalist.get(mCurrPos);
            Bundle trackdata =  new Bundle();
            trackdata.putString(PARAM_ARTIST, mArtistName);
            trackdata.putString(PARAM_ALBUM, track.album.name);
            trackdata.putString(PARAM_TRACK, track.name);
            trackdata.putString(PARAM_TRACKURL, track.preview_url);
            trackdata.putString(PARAM_ARTISTID, mArtistId);
            trackdata.putString(PARAM_TRACKID, track.id);
            trackdata.putString(PARAM_ALBUMART, track.album.images.get(0).url);
            return trackdata;
        } else {
            return null;
        }
    }

    public void showPlayer(Bundle trackdata) {
        DialogFragment player = new PlayerDialogFragment();
        player.setArguments(trackdata);
        if(mIsLargeScreen) {
            // show fragment in modal window
            player.show(getActivity().getSupportFragmentManager(), PlayerDialogFragment.FRAGMENT_NAME);
        } else {
            // show fragment fullscreen on smaller devices
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(android.R.id.content, player)
                .addToBackStack(PlayerDialogFragment.FRAGMENT_NAME)
                .commit();
        }
    }
}