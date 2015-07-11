package com.vbz.spotifystreamer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String[] data = {"abdac", "dadsefg", "hijkik", "orujf", "dddd"};
    private static final String LOG_TAG = "SPOTSTREAMER";

    private void handleIntent(Intent intent) {
        // TODO: this should be done in background thread
        Log.d(LOG_TAG, "searched called");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //dummy data for testing
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // TODO: get real data from spotify wrapper here
            List<String> found = new ArrayList<String>();
            for (String s : data) {
                if (s.indexOf(query) != 1) {
                    found.add(s);
                }
            }
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++

            if (found.size() > 0) {
                // list data found
                //TODO: pass arraylist to to adapter for display
                //String[] res = found.toArray(new String[found.size()]);
                Log.d(LOG_TAG, "data found: " + found.toString());
                Toast.makeText(this, "found: " + found.toString(), Toast.LENGTH_SHORT).show();
            } else {
                // toast no data found
                Log.e(LOG_TAG, "no data found");
                Toast.makeText(this, "no data found for [" + query + "]", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }
        Log.d(LOG_TAG, "handling search in onCreate");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG_TAG, "handling search in onNewIntent");
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.artist_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
