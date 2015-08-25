package com.vbz.spotifystreamer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vbz.spotifystreamer.data.SpotifyDataContract.ArtistEntry;
import com.vbz.spotifystreamer.data.SpotifyDataContract.TrackEntry;

public class SpotifyCacheReader {
    private SQLiteDatabase mDb = null;
    private final String mQueryTracks = "SELECT "
            + " a." + ArtistEntry.COLUMN_ARTISTID + ", "
            + " a." + ArtistEntry.COLUMN_ARTISTNAME + ", "
            + " t." + TrackEntry.COLUMN_TRACKID + ", "
            + " t." + TrackEntry.COLUMN_TRACKNAME + ", "
            + " t." + TrackEntry.COLUMN_TRACKALBUM + ", "
            + " t." + TrackEntry.COLUMN_TRACKIMG + ", "
            + " t." + TrackEntry.COLUMN_TRACKPLAYBACKURL
            + " FROM " + TrackEntry.TABLE_NAME + " t JOIN "
            + ArtistEntry.TABLE_NAME + " a ON t."
            + TrackEntry.COLUMN_ARTISTID + " = a." + ArtistEntry._ID
            + " WHERE a." + ArtistEntry.COLUMN_ARTISTID + "=?;";
    private final String mQueryTrackIdx = null;

    public SpotifyCacheReader(Context ctx) {
        mDb = new SpotifyCacheDb(ctx).getReadableDatabase();
        Log.d("CACHEREADER", mQueryTracks);
    }

    public Cursor getTracks(String artistid) {
        Cursor c = mDb.rawQuery(mQueryTracks, new String[] {artistid});
        // TODO: check if cursor is empty, return null
        return c;
    }

    public long getTrackIndex(String trackid) {
        Cursor c = mDb.rawQuery(mQueryTrackIdx, new String[] {trackid});
        // TODO: define mQueryTrackIdx, retrieve index here to return
        long idx = 1;
        return idx;
    }
}
