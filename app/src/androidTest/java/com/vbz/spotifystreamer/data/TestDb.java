package com.vbz.spotifystreamer.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vbz.spotifystreamer.data.SpotifyDataContract.ArtistEntry;
import com.vbz.spotifystreamer.data.SpotifyDataContract.TrackEntry;

/* appropriated from sunshine app: https://github.com/udacity/Sunshine-Version-2/tree/lesson_4_starter_code */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName().toUpperCase();

    void deleteTheDatabase() {
        mContext.deleteDatabase(SpotifyDataStore.DATABASE_NAME);
    }

    public long insertRecord(ContentValues data, SQLiteDatabase db, String tableName) {
        long rowid = db.insert(tableName, null, data);
        assertTrue("Error: during row insertion", rowid != -1);
        return rowid;
    }

    public void validateRecord(ContentValues expectedValues, SQLiteDatabase db, String tableName) {
        // Query the database and receive a Cursor back
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        assertTrue("Error: cannot retrieve insterted data", c.moveToFirst());
        String errmsg = "inserted row does not match original data";

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = c.getColumnIndex(columnName);
            assertFalse("Error: " + tableName + "." + columnName + " not found. " + errmsg, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString()
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + errmsg, expectedValue, c.getString(idx));
        }
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        Log.d(LOG_TAG, "test db creation...");
        final HashSet<String> tableNames = new HashSet<String>();
        tableNames.add(ArtistEntry.TABLE_NAME);
        tableNames.add(TrackEntry.TABLE_NAME);
        SQLiteDatabase db = new SpotifyDataStore(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // verify that the tables have been created
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: database was not created correctly", c.moveToFirst());
        do { tableNames.remove(c.getString(0)); } while(c.moveToNext());
        assertTrue("Error: missing table(s) in db", tableNames.isEmpty());

        // check artist table contains the expected columns
        c = db.rawQuery("PRAGMA table_info(" + ArtistEntry.TABLE_NAME + ")", null);
        assertTrue("Error: unable to query db for " + ArtistEntry.TABLE_NAME
                + " table information.", c.moveToFirst());
        final HashSet<String> ArtistColumns = new HashSet<String>();
        ArtistColumns.add(ArtistEntry._ID);
        ArtistColumns.add(ArtistEntry.COLUMN_ARTISTID);
        ArtistColumns.add(ArtistEntry.COLUMN_ARTISTNAME);
        ArtistColumns.add(ArtistEntry.COLUMN_ARTISTIMG);
        // we remove the expected columns from the hashset, what should be left is an empty set
        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            ArtistColumns.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: table " + ArtistEntry.TABLE_NAME + " is missing columns",
                   ArtistColumns.isEmpty());

        // check track table contains the expected columns
        c = db.rawQuery("PRAGMA table_info(" + TrackEntry.TABLE_NAME + ")", null);
        assertTrue("Error: unable to query db for " + TrackEntry.TABLE_NAME
                + " table information.", c.moveToFirst());
        final HashSet<String> trackColumns = new HashSet<String>();
        trackColumns.add(TrackEntry._ID);
        trackColumns.add(TrackEntry.COLUMN_TRACKID);
        trackColumns.add(TrackEntry.COLUMN_ARTISTID);
        trackColumns.add(TrackEntry.COLUMN_TRACKNAME);
        trackColumns.add(TrackEntry.COLUMN_TRACKALBUM);
        trackColumns.add(TrackEntry.COLUMN_TRACKIMG);
        trackColumns.add(TrackEntry.COLUMN_TRACKPLAYBACKURL);
        // we remove the expected columns from the hashset, what should be left is an empty set
        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            trackColumns.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: table " + ArtistEntry.TABLE_NAME + " is missing columns",
                trackColumns.isEmpty());

        // release resources
        db.close();
    }

    public void testArtistTable() {
        // Insert ContentValues into database and get a row ID back
        Log.d(LOG_TAG, "test artist table insert...");
        SQLiteDatabase db = new SpotifyDataStore(mContext).getWritableDatabase();

        ContentValues artistdata = new ContentValues();
        artistdata.put(ArtistEntry.COLUMN_ARTISTID, "123");
        artistdata.put(ArtistEntry.COLUMN_ARTISTNAME, "DRAKE");
        artistdata.put(ArtistEntry.COLUMN_ARTISTIMG, "http://images.com/artist/drake.jpg");

        insertRecord(artistdata, db, ArtistEntry.TABLE_NAME);
        validateRecord(artistdata, db, ArtistEntry.TABLE_NAME);

        db.close();
    }

    public void testTrackTable() {
        Log.d(LOG_TAG, "test track table insert...");
        SQLiteDatabase db = new SpotifyDataStore(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // we first insert artist data, because it's referenced in track table
        ContentValues artistdata = new ContentValues();
        artistdata.put(ArtistEntry.COLUMN_ARTISTID,   "456");
        artistdata.put(ArtistEntry.COLUMN_ARTISTNAME, "Drake");
        artistdata.put(ArtistEntry.COLUMN_ARTISTIMG,  "http://images.com/artist/drake.jpg");
        long aRowId = insertRecord(artistdata, db, ArtistEntry.TABLE_NAME);

        ContentValues trackdata = new ContentValues();
        trackdata.put(TrackEntry.COLUMN_TRACKID,          "123");
        trackdata.put(TrackEntry.COLUMN_ARTISTID,         aRowId);
        trackdata.put(TrackEntry.COLUMN_TRACKNAME,        "Hotline Bling");
        trackdata.put(TrackEntry.COLUMN_TRACKALBUM,       "Nothing Was The Same");
        trackdata.put(TrackEntry.COLUMN_TRACKIMG,         "http://images.com/album/nothing_was_the_same.jpg");
        trackdata.put(TrackEntry.COLUMN_TRACKPLAYBACKURL, "http://streamify.com/hotline_bling.mp3");

        insertRecord(trackdata, db, TrackEntry.TABLE_NAME);
        validateRecord(trackdata, db, TrackEntry.TABLE_NAME);

        db.close();
    }
}
