package com.vbz.spotifystreamer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vbz.spotifystreamer.data.SpotifyDataStore;
import com.vbz.spotifystreamer.data.SpotifyDataContract.ArtistEntry;
import com.vbz.spotifystreamer.data.SpotifyDataContract.TrackEntry;

public class MockData {
    /**
     * creates dummy data used for
     * @param ctx context to pass to database
     */
    public void create(Context ctx) {
//        return;

        // dummy data used for development
        SQLiteDatabase db = new SpotifyDataStore(ctx).getWritableDatabase();

        String[] columns = null;
        String[] selectionargs = {"999"};
        String table = ArtistEntry.TABLE_NAME;
        String selection= ArtistEntry.COLUMN_ARTISTID + "=?";
        String groupby, having, orderby;
        groupby = having = orderby = null;

        Cursor c = db.query(table, columns, selection, selectionargs, groupby, having, orderby);
        if (!c.moveToFirst()) {
            ContentValues artistdata = new ContentValues();
            artistdata.put(ArtistEntry.COLUMN_ARTISTID, "999");
            artistdata.put(ArtistEntry.COLUMN_ARTISTNAME, "mokieDrake");
            artistdata.put(ArtistEntry.COLUMN_ARTISTIMG, "http://images.com/artist/drake.jpg");
            long idx = db.insert(ArtistEntry.TABLE_NAME, null, artistdata);

            ContentValues trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "111");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "Hotline Bling");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://images.com/album/nothing_was_the_same.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://streamify.com/hotline_bling.mp3");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);

            trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "222");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "Thank Me Later");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://images.com/album/thank_me_later.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://streamify.com/thank_me_later.mp3");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);

            trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "333");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "6 God");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://images.com/album/6_god.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://streamify.com/6_god.mp3");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);
        }

        c.close();
        db.close();
    }
}
