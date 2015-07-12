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
public class ListViewAdapter extends ArrayAdapter<ListViewItem> {

    public ListViewAdapter(Context context, List<ListViewItem> items) {
        super(context, R.layout.listview_item, items);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

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
        ListViewItem item = getItem(position);
        viewHolder.ivCover.setImageDrawable(item.cover);
        viewHolder.tvName.setText(item.name);

        return convertView;
    }

    private static class ViewHolder {
        ImageView ivCover;
        TextView tvName;
    }
}
