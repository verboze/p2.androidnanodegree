package com.vbz.spotifystreamer;

import android.graphics.drawable.Drawable;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class ArtistListViewItem {
    public final Drawable cover;
    public final String name;

    public ArtistListViewItem(Drawable icon, String name) {
        this.cover = icon;
        this.name = name;
    }
}
