package com.vbz.spotifystreamer;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kagbodji on 6/27/15.
 */
public class MainWorker extends AsyncTask<String, Void, String[]> {
    private final String LOG_TAG = MainWorker.class.getSimpleName();
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String apiResponse = null;

    @Override
    protected String[] doInBackground(String... params) {
        try {
            // build query URL
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.openweathermap.org")
                    .appendEncodedPath("data/2.5/forecast")
                    .appendPath(params[0]) // TODO: parameterize
                    .appendQueryParameter("q", params[1]) // TODO: parameterize
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", "7");
            String myurl = builder.build().toString();
            Log.d(LOG_TAG, "API QUERY URL: " + myurl);
            URL url = new URL(myurl);

            // open connection to get data
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.i(LOG_TAG, "INFO: no response from API");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                Log.i(LOG_TAG, "INFO: empty buffer returned from API");
                return null;
            }
            apiResponse = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR: api call failed:\n", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.v(LOG_TAG, "API RESPONSE (JSON): " + apiResponse);

        String[] out;
        try {
            out = parseJson(apiResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            out = new String[0];
        }
        return out;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        //TODO: refresh adapter. where do I get the adpater from?!?
        //adpater.clear();
        //for(String i : strings) { adpater.add(i); }
        //adapter.notifyDataSetChanged();
    }

    /**
     * parse response from Spotify API
     */
    private String[] parseJson(String strdata)
            throws JSONException {
        //TODO: implement
        return null;
    }
}
