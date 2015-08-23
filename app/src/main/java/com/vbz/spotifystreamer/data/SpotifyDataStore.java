package com.vbz.spotifystreamer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vbz.spotifystreamer.data.SpotifyDataContract.ArtistEntry;
import com.vbz.spotifystreamer.data.SpotifyDataContract.TrackEntry;

public class SpotifyDataStore extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "spotifycache.db";
    private static final int DATABASE_VERSION = 1;

    public SpotifyDataStore(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ARTISTS_TABLE = "CREATE TABLE "
                + ArtistEntry.TABLE_NAME        + " ("
                + ArtistEntry._ID               + " INTEGER PRIMARY KEY, "
                + ArtistEntry.COLUMN_ARTISTID   + " TEXT UNIQUE NOT NULL, "
                + ArtistEntry.COLUMN_ARTISTNAME + " TEXT NOT NULL, "
                + ArtistEntry.COLUMN_ARTISTIMG  + " TEXT);";

        final String SQL_CREATE_TRACKS_TABLE = "CREATE TABLE "
                + TrackEntry.TABLE_NAME              + "("
                + TrackEntry._ID                     + " INTEGER PRIMARY KEY, "
                + TrackEntry.COLUMN_TRACKID          + " TEXT NOT NULL, "
                + TrackEntry.COLUMN_ARTISTID         + " TEXT NOT NULL, "
                + TrackEntry.COLUMN_TRACKNAME        + " TEXT NOT NULL, "
                + TrackEntry.COLUMN_TRACKALBUM       + " TEXT, "
                + TrackEntry.COLUMN_TRACKIMG         + " TEXT, "
                + TrackEntry.COLUMN_TRACKPLAYBACKURL + " TEXT, "
                + "FOREIGN KEY (" + TrackEntry.COLUMN_ARTISTID + ") REFERENCES "
                + ArtistEntry.TABLE_NAME + "(" + ArtistEntry._ID + "), "
                + "UNIQUE(" + TrackEntry.COLUMN_ARTISTID + ", " + TrackEntry.COLUMN_TRACKNAME
                + ", " + TrackEntry.COLUMN_TRACKALBUM + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_ARTISTS_TABLE);
        db.execSQL(SQL_CREATE_TRACKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
