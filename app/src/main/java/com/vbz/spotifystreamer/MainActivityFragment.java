package com.vbz.spotifystreamer;

import android.content.Context;
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
    private final String[] data = {"elisabeth", "nanako", "kate", "adama", "kerri"};
    private static final String LOG_TAG = "SPOTSTREAMER";

    private void search(String query) {
        // TODO: this should be done in background thread
        if (! TextUtils.isEmpty(query)) {
            Log.d(LOG_TAG, "(fragment) searched called");
            //dummy data for testing
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // TODO: get real data from spotify wrapper here
            List<String> found = new ArrayList<String>();
            for (String s : data) {
                if (s.contains(query)) {
                    found.add(s);
                }
            }
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++

            if (found.size() > 0) {
                // list data found
                //TODO: pass arraylist to to adapter for display
                Log.d(LOG_TAG, "(fragment) data found: " + found.toString());
                Toast.makeText(getActivity(), " fragment) data found: " + found.toString(), Toast.LENGTH_SHORT).show();
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
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
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
//        setEmptyText(getString(R.string.search_hint));
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        Log.d(LOG_TAG, "Item clicked: " + id);
    }
}
