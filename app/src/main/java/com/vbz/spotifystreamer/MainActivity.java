package com.vbz.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {
    private static final String ARTIST_FRAGMENT = "artist_fragment";
    private ArtistViewFragment artistfrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.maincontainer, new ArtistViewFragment())
                    .commit();
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

// TODO: NICE TO HAVES:
//       display message in listview on no data found
//       show loader when data is being fetched
