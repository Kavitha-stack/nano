package com.android.udacity.kavitha.popularmoviesstage1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Movie> {

    Context context;

    public CustomAdapter(Context context, int resourceId,
                         List<Movie> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        ImageView imageView;
        try {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_movies, null);
            }
            imageView = (ImageView) view.findViewById(R.id.imageView);
            Movie m = getItem(position);
            String url = context.getString(R.string.main_view_image_url) + m.getPosterUrl();
            Picasso.with(context).load(url).placeholder(R.drawable.loader).error(R.drawable.loader).fit().centerCrop().into(imageView);
            return imageView;
        } catch (Exception ex) {
            System.out.println("Exception in adapter" + ex.getMessage());
        }
        return view;
    }
}