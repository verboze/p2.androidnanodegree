package com.vbz.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
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
    private static final String LOG_TAG_APP = "SPOTSTREAMER";
    private static final String LOG_TAG_API = "SPOTAPI";
    private List<Track> datalist; // holds data retrieved from API
    private SpotifyService spotifysvc = new SpotifyApi().getService();
    private boolean was_data_fetched = false;

    public TrackViewFragment() {

    }

    private void fetchTracks(String artistid) {
        // TODO: retrieve data from network using spotify API
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
        if (savedinstanceSate != null) {
            // restore layout
            Log.d(LOG_TAG_APP, "restoring instance...?");
        } else {
            datalist = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View v = inflater.inflate(R.layout.fragment_tracks, container, false);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            String artistName = intent.getStringExtra("artistname");
            ab.setSubtitle(artistName);
        }

        if (!was_data_fetched) {
            String artistId = intent.getStringExtra("artistid");
            fetchTracks(artistId);
            was_data_fetched = true;
        }
        return v;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        Track item = datalist.get(position);
        Toast.makeText(getActivity(), "Item clicked: " + item.name, Toast.LENGTH_SHORT).show();
    }
}
