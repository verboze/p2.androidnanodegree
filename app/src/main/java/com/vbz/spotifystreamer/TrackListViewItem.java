package com.vbz.spotifystreamer;

import android.graphics.drawable.Drawable;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class TrackListViewItem {
    public final Drawable cover;
    public final String album;
    public final String track;

    public TrackListViewItem(Drawable icon, String album, String track) {
        this.cover = icon;
        this.album = album;
        this.track = track;
    }
}
