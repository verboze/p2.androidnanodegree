package com.vbz.spotifystreamer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends ListFragment {
    private List<ListViewItem> datalist; // holds data retrieved from API

    // DUMMY TEST DATA
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private final String[] data = {"adama", "elisabeth", "kate", "kerri", "nanako"};
    private final String[] pics = new String[] { "http://www.smashingmagazine.com/images/music-cd-covers/67.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/64.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/michael_jackson_dangerous-f.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/43.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/zeitgeist.jpg" };
    private static final String LOG_TAG = "SPOTSTREAMER";
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void search(String query) {
        // TODO: retrieve data from network using spotify API
        Log.d(LOG_TAG, "(fragment) searched called");
        datalist.clear();
        Resources resresolver = getResources(); // KDADEBUG: used to temp resolve local images

        if (! TextUtils.isEmpty(query)) {
            // TODO: get real data from spotify wrapper here
            List<String> found = new ArrayList<String>();
            for (String s : data) {
                if (s.contains(query)) {
                    found.add(s);
                }
            }

            if (found.size() > 0) {
                Log.d(LOG_TAG, "(fragment) data found: " + found.toString());
                Toast.makeText(getActivity(), " fragment) data found: " + found.toString(), Toast.LENGTH_SHORT).show();
                for (String i : found) {
                    datalist.add(new ListViewItem(resresolver.getDrawable(R.drawable.ic_launcher), i));
                }
                setListAdapter(new ListViewAdapter(getActivity(), datalist));
            } else {
                // toast no data found
                Log.d(LOG_TAG, "(fragment) no data found");
                Toast.makeText(getActivity(), "(fragment) no data found for [" + query + "]", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(LOG_TAG, "(fragment) empty search query");
        }
    }

    public MainActivityFragment() {
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
        setHasOptionsMenu(true);
        datalist = new ArrayList<ListViewItem>();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // set array adapter with empty data
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // let the fragment resolve searches
        // we get the theme for this activity so that text is rendered properly
        Context themectx = ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        SearchView sv = new SearchView(themectx);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                Log.d(LOG_TAG, "submitting query: " + query);
                search(query);
                return true;
            }

            @Override public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG, "listening for... " + newText);
                return true;
            }
        });
        sv.setQueryHint("Search for an artist...");
        sv.setOnCloseListener(null);
        sv.setIconifiedByDefault(true);

        // add a search icon to action bar
        inflater.inflate(R.menu.menu_mainfragment, menu);
        MenuItem searchview = menu.findItem(R.id.search);
        searchview.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchview.setActionView(sv);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        ListViewItem item = datalist.get(position);
        Toast.makeText(getActivity(), "Item clicked: " + item.name, Toast.LENGTH_SHORT).show();
    }
}
