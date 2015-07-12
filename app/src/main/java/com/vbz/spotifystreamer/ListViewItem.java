package com.vbz.spotifystreamer;

import android.graphics.drawable.Drawable;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class ListViewItem {
    public final Drawable cover;
    public final String name;

    public ListViewItem(Drawable icon, String name) {
        this.cover = icon;
        this.name = name;
    }
}
