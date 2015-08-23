package com.vbz.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ArtistViewFragment.SelectionCallback {
    private static final String ARTIST_FRAGMENT = "artist_fragment";
    private boolean mTwoPanes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.tracks_fragment_container) != null) {
            mTwoPanes = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.tracks_fragment_container, new TrackViewFragment())
                        .commit();
            }
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public void onItemSelected(Bundle data) {
        // handle list item click, dispatch track detail to either
        // a new activity or to an adjacent fragment
        // TODO: IMPORTANT! fix 'rotate from landscape to normal' crashes app No view found for id 0x7f0c0051
        if (mTwoPanes) {
            TrackViewFragment tvf = new TrackViewFragment();
            tvf.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tracks_fragment_container, tvf, TrackViewFragment.FRAGMENT_NAME)
                    .commit();
        } else {
            Intent detailsIntent = new Intent(this, TrackActivity.class);
            detailsIntent.putExtra("artistname", data.getString("artistname"));
            detailsIntent.putExtra("artistid", data.getString("artistid"));

            if (detailsIntent.resolveActivity(this.getPackageManager()) != null) {
                startActivity(detailsIntent);
            } else {
                Toast.makeText(this, "could not find TackActivity", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

// TODO: NICE TO HAVES:
//       display message in listview on no data found
//       show loader when data is being fetched
