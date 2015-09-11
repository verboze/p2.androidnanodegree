package com.vbz.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class TrackListViewAdapter extends ArrayAdapter<Track> {
    private static final String emptycoverurl = "http://static.tumblr.com/jn9hrij/20Ul2zzsr/albumart.jpg";

    public TrackListViewAdapter(Context context, List<Track> items) {
        super(context, R.layout.listview_track_item, items);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_track_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.list_item_img);
            viewHolder.tvAlbum = (TextView) convertView.findViewById(R.id.list_item_album);
            viewHolder.tvTrack = (TextView) convertView.findViewById(R.id.list_item_track);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Track item = getItem(position);
        viewHolder.tvAlbum.setText(item.album.name);
        viewHolder.tvTrack.setText(item.name);

        if (item.album.images.size() > 0) {
            Picasso.with(getContext())
                    .load(item.album.images.get(0).url)
                    .resize(100, 100)
                    .into(viewHolder.ivCover);
        } else {
            Picasso.with(getContext())
                    .load(emptycoverurl)
                    .resize(100, 100)
                    .into(viewHolder.ivCover);
        }


        return convertView;
    }

    private static class ViewHolder {
        ImageView ivCover;
        TextView tvAlbum;
        TextView tvTrack;
    }
}
