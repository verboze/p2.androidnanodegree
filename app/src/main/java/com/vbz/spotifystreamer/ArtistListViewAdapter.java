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

import kaaes.spotify.webapi.android.models.Artist;

/* adapted from: http://www.perfectapk.com/android-listfragment-tutorial.html */
public class ArtistListViewAdapter extends ArrayAdapter<Artist> {
    private static final String emptyartisturl = "https://pbs.twimg.com/profile_images/2481881216/i29qdpcln2fiakaoii4p_400x400.gif";

    public ArtistListViewAdapter(Context context, List<Artist> items) {
        super(context, R.layout.listview_artist_item, items);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_artist_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.list_item_img);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.list_item_text);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        Artist item = getItem(position);
        viewHolder.tvName.setText(item.name);
        if (item.images.size() > 0) {
            Picasso.with(getContext())
                    .load(item.images.get(0).url)
                    .resize(100, 100)
                    .into(viewHolder.ivCover);
        } else {
            Picasso.with(getContext())
                    .load(emptyartisturl)
                    .resize(100, 100)
                    .into(viewHolder.ivCover);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivCover;
        TextView tvName;
    }
}
