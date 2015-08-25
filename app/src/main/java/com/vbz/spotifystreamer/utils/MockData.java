package com.vbz.spotifystreamer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vbz.spotifystreamer.data.SpotifyCacheDb;
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
        SQLiteDatabase db = new SpotifyCacheDb(ctx).getWritableDatabase();

        String[] columns = null;
        String[] selectionargs = {"999"};
        String table = ArtistEntry.TABLE_NAME;
        String selection= ArtistEntry.COLUMN_ARTISTID + "=?";
        String groupby, having, orderby;
        groupby = having = orderby = null;

        Cursor c = db.query(table, columns, selection, selectionargs, groupby, having, orderby);
        if (!c.moveToFirst()) {
            Log.d("MOCKDATA", "creating mock data...");

            ContentValues artistdata = new ContentValues();
            artistdata.put(ArtistEntry.COLUMN_ARTISTID, "999");
            artistdata.put(ArtistEntry.COLUMN_ARTISTNAME, "mock - Drake");
            artistdata.put(ArtistEntry.COLUMN_ARTISTIMG, "http://www.albumartexchange.com/gallery/images/public/dr/_drake-righth.tn.jpg");
            long idx = db.insert(ArtistEntry.TABLE_NAME, null, artistdata);

            ContentValues trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "111");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "mock - Hotline Bling");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://www.albumartexchange.com/gallery/images/public/ca/_canned-back2b.tn.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://listen.radionomy.com/-1Beats");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);

            trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "222");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "mock - Thank Me Later");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://www.albumartexchange.com/gallery/images/public/mc/_mcarey-butter_10.tn.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://listen.radionomy.com:80/R-n-BCream");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);

            trackdata = new ContentValues();
            trackdata.put(TrackEntry.COLUMN_TRACKID, "333");
            trackdata.put(TrackEntry.COLUMN_ARTISTID, idx);
            trackdata.put(TrackEntry.COLUMN_TRACKNAME, "mock - 6 God");
            trackdata.put(TrackEntry.COLUMN_TRACKALBUM, "Nothing Was The Same");
            trackdata.put(TrackEntry.COLUMN_TRACKIMG, "http://www.albumartexchange.com/gallery/images/public/ca/_camel-echoes_07.tn.jpg");
            trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://listen.radionomy.com/RGMediaRadio");
            db.insert(TrackEntry.TABLE_NAME, null, trackdata);
        }

        c.close();
        db.close();
    }

    public void delete(Context ctx) {
        Log.d("MOCKDATA", "removing mock data...");

        String sql1 = "delete from " + ArtistEntry.TABLE_NAME + " where " + ArtistEntry.COLUMN_ARTISTNAME + " like 'mock - %';";
        String sql2 = "delete from " + TrackEntry.TABLE_NAME + " where " + TrackEntry.COLUMN_TRACKNAME + " like 'mock - %';";

        sql1 = "delete from " + ArtistEntry.TABLE_NAME + ";";
        sql2 = "delete from " + TrackEntry.TABLE_NAME + ";";

        SQLiteDatabase db = new SpotifyCacheDb(ctx).getWritableDatabase();
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.close();
    }
}
