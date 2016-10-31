package com.android.udacity.kavitha.popularmoviesstage2.fragments;

/**
 * Created by Kavitha on 9/14/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.udacity.kavitha.popularmoviesstage2.R;
import com.android.udacity.kavitha.popularmoviesstage2.data.MovieContract;
import com.squareup.picasso.Picasso;


public class MovieAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private static int sLoaderID;

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView title;
        public final TextView rating;
        public final TextView releaseDate;
        public final TextView overview;
        public final ImageButton favorite;


        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView);
            title = (TextView) view.findViewById(R.id.movie_item_title);
            rating = (TextView) view.findViewById(R.id.rating);
            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
            overview = (TextView) view.findViewById(R.id.overview);
            favorite = (ImageButton) view.findViewById(R.id.imagefavouriteButton);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags, int loaderID) {
        super(context, c, flags);
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_movies;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int imageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        String imageUrl = cursor.getString(imageIndex);
        String url = context.getString(R.string.main_view_image_url) + imageUrl;
        Picasso.with(context).load(url).placeholder(R.drawable.loader).error(R.drawable.loader).fit().centerCrop().into(viewHolder.imageView);
    }

}

