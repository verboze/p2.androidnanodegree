package com.vbz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
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

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistViewFragment extends ListFragment {
    private static final String LOG_TAG = "SPOTSTREAMER";
    private List<ArtistListViewItem> datalist; // holds data retrieved from API
    private SpotifyApi spotApi = new SpotifyApi();

    public ArtistViewFragment() {

    }

    // DUMMY TEST DATA
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private final String[] data = {"adama", "elisabeth", "kate", "kerri", "nanako"};
    private final String[] pics = new String[] { "http://www.smashingmagazine.com/images/music-cd-covers/67.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/64.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/michael_jackson_dangerous-f.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/43.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/zeitgeist.jpg" };
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private List<String[]> search(String query) {
        // TODO: retrieve data from network using spotify API
        final List<Artist> foundartists = new ArrayList<Artist>();

        SpotifyService spotify = spotApi.getService();
        spotify.searchArtists(query, new Callback<ArtistsPager>() {
            @Override public void success(ArtistsPager artists, Response response) {
                List<Artist> artistlist = artists.artists.items;
                Log.d("SPOTAPI", "found artists [" + artistlist.size() + "]: " + artistlist.toString());
                for (Artist i : artists.artists.items) {
                    foundartists.add(i);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("Album failure", error.toString());
            }
        });
        // --------------------------------------------------------
        Log.d(LOG_TAG, "search called");
        List<String[]> found = new ArrayList<String[]>();

        if (! TextUtils.isEmpty(query)) {
            // TODO: get real data from spotify wrapper here
            for (String s : data) {
                if (s.contains(query)) {
                    String[] a = {"1", s};
                    found.add(a);
                }
            }
        }

        return found;
    }

    public void setListdata(List<String[]> data) {
        datalist.clear();
        Resources resresolver = getResources(); // KDADEBUG: used to temp resolve local images

        if (data.size() > 0) {
            Log.d(LOG_TAG, "data found: " + data.toString());
            for (String[] i : data) {
                datalist.add(new ArtistListViewItem(resresolver.getDrawable(R.drawable.ic_launcher), i[1]));
            }
            setListAdapter(new ArtistListViewAdapter(getActivity(), datalist));
//            setListShown(true);
        } else {
            // toast no data found
//            setListShown(false);
//            setEmptyText("no data found...");
            Log.d(LOG_TAG, "no data found");
//            Toast.makeText(getActivity(), "no data found for [" + query + "]", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        datalist = new ArrayList<ArtistListViewItem>();
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
                Log.d(LOG_TAG, "submitting query: " + query);
                List<String[]> data = search(query);
                setListdata(data);
                sv.onActionViewCollapsed(); // collapses searchbar and hides keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG, "listening for... " + newText);
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
        ArtistListViewItem item = datalist.get(position);

        Intent detailsIntent = new Intent(getActivity(), TrackActivity.class);
        detailsIntent.putExtra("artist", item.name);
        if (detailsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(detailsIntent);
        }
        else {
            Toast.makeText(getActivity(), "could not find TackActivity", Toast.LENGTH_SHORT).show();
        }
    }
}
