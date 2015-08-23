package com.vbz.spotifystreamer.data;

import android.provider.BaseColumns;

public class SpotifyDataContract {
    public static final class ArtistEntry implements BaseColumns {
        public static final String TABLE_NAME = "artists";
        public static final String COLUMN_ARTISTID = "artistid";
        public static final String COLUMN_ARTISTNAME = "artistname";
        public static final String COLUMN_ARTISTIMG = "artistimg";
    }

    public static final class TrackEntry implements BaseColumns {
        public static final String TABLE_NAME = "tracks";
        public static final String COLUMN_TRACKID = "trackid";
        public static final String COLUMN_ARTISTID = "artistid";
        public static final String COLUMN_TRACKNAME = "trackname";
        public static final String COLUMN_TRACKALBUM = "trackalbum";
        public static final String COLUMN_TRACKIMG = "trackimg";
        public static final String COLUMN_TRACKPLAYBACKURL = "trackplaybackurl";
    }
}
