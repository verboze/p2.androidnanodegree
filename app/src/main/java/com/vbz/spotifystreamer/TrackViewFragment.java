package com.vbz.spotifystreamer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TrackViewFragment extends ListFragment {
    private static final String LOG_TAG = "SPOTSTREAMER";
    private List<TrackListViewItem> datalist; // holds data retrieved from API

    public TrackViewFragment() {

    }

    private List<String[]> fetchTracks(String query) {
        // TODO: retrieve data from network using spotify API
        Toast.makeText(getActivity(), "looking up tracks for " + query + "...", Toast.LENGTH_SHORT).show();
        // dummy data ++++++++++++++++++++++++++++++++++++++++++++++++
        ArrayList<String[]> found = new ArrayList<String[]>();
        int itemcount = 3;
        for (int i=0; i < itemcount; i++) {
            String[] m = {"key"+i, "val"+i};
            found.add(m);
        }
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        return found;
    }

    public void setListdata(List<String[]> data) {
        datalist.clear();
        Resources resresolver = getResources(); // KDADEBUG: used to temp resolve local images

        if (data.size() > 0) {

            for (String[] i : data) {
                datalist.add(new TrackListViewItem(resresolver.getDrawable(R.drawable.ic_launcher), i[0], i[1]));
            }
            setListAdapter(new TrackListViewAdapter(getActivity(), datalist));
        } else {
            // toast no data found
            setEmptyText("no data found...");
            Log.d(LOG_TAG, "no data found");
        }
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
//        setRetainInstance(true);
        datalist = new ArrayList<TrackListViewItem>();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // set array adapter with empty data
        View v =  inflater.inflate(R.layout.fragment_main, container, false);
        Intent intent = getActivity().getIntent();

        String artist = intent.getStringExtra("artist");
        List<String[]> tracks = fetchTracks(artist);
        setListdata(tracks);
        return v;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        TrackListViewItem item = datalist.get(position);
        Toast.makeText(getActivity(), "Item clicked: " + item.album, Toast.LENGTH_SHORT).show();
    }
}
