package com.vbz.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class TrackListViewAdapter extends ArrayAdapter<TrackListViewItem> {

    public TrackListViewAdapter(Context context, List<TrackListViewItem> items) {
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
        TrackListViewItem item = getItem(position);
        viewHolder.ivCover.setImageDrawable(item.cover);
        viewHolder.tvAlbum.setText(item.album);
        viewHolder.tvTrack.setText(item.track);

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivCover;
        TextView tvAlbum;
        TextView tvTrack;
    }
}
