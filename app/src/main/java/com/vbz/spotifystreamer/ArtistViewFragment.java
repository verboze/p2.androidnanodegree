package com.vbz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistViewFragment extends ListFragment {
    private static final String LOG_TAG_APP = "SPOTSTREAMER";
    private static final String LOG_TAG_API = "SPOTAPI";
    private List<Artist> datalist; // holds data retrieved from API
    private SpotifyService spotifysvc = new SpotifyApi().getService();

    public ArtistViewFragment() {

    }

    private void search(String query) {
        // TODO: make it a bit more user friendly, show spinners while loading, etc
        final List<Artist> foundartists = new ArrayList<Artist>();
        final String usrquery = query;

        spotifysvc.searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artists, Response response) {
                List<Artist> artistlist = artists.artists.items;
                Log.d(LOG_TAG_API, "found artists [" + artistlist.size() + "]: " + artistlist.toString());
                datalist.clear();

                if (artistlist.size() > 0) {
                    setListdata(artistlist);
                } else {
                    Toast.makeText(getActivity(), "no artists found by the name: " + usrquery, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG_API, "failed to retrieve artists:\n" + error.toString());
                Toast.makeText(getActivity(), "failed to retrieve artists. Possible network issues?: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setListdata(List<Artist> data) {
        Log.d(LOG_TAG_APP, "data found: " + data.toString());
        for (Artist i : data) {
            datalist.add(i);
        }
        setListAdapter(new ArtistListViewAdapter(getActivity(), datalist));
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (savedinstanceSate != null) {
            // restore layout
            Log.d(LOG_TAG_APP, "restoring instance...?");
        } else {
            datalist = new ArrayList<Artist>();
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // set array adapter with empty data
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // add a search icon to action bar
        inflater.inflate(R.menu.menu_mainfragment, menu);
        final MenuItem searchButton = menu.findItem(R.id.search);
        searchButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        // bind processing of searchview results to this fragment
        // we get the theme for this activity so that text is rendered properly
        Context themectx = ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        final SearchView sv = new SearchView(themectx);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(LOG_TAG_APP, "submitting query: " + query);
                search(query);
                sv.onActionViewCollapsed(); // collapses searchbar and hides keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG_APP, "listening for... " + newText);
                return true;
            }
        });
        sv.setQueryHint("Search for an artist...");
        sv.setOnCloseListener(null);
        sv.setIconifiedByDefault(true);

        searchButton.setActionView(sv);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        Artist item = datalist.get(position);

        Intent detailsIntent = new Intent(getActivity(), TrackActivity.class);
        detailsIntent.putExtra("artist", item.id);
        if (detailsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(detailsIntent);
        }
        else {
            Toast.makeText(getActivity(), "could not find TackActivity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.put
        super.onSaveInstanceState(savedInstanceState);
    }
}
