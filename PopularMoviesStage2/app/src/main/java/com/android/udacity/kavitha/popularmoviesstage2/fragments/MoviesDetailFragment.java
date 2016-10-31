package com.android.udacity.kavitha.popularmoviesstage2.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.udacity.kavitha.popularmoviesstage2.R;
import com.android.udacity.kavitha.popularmoviesstage2.activities.MainActivity;
import com.android.udacity.kavitha.popularmoviesstage2.service.VolleyController;
import com.android.udacity.kavitha.popularmoviesstage2.data.MovieContract;
import com.android.udacity.kavitha.popularmoviesstage2.model.Movie;
import com.android.volley.toolbox.ImageLoader;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import java.io.InputStream;

/**
 * Created by Kavitha on 10/8/2016.
 */
public class MoviesDetailFragment extends Fragment {

    public ContentValues setMovies(Movie movie, String favorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movie.getBackdropUrl());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, String.valueOf(movie.getPopularity()));
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterUrl());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, String.valueOf(movie.getVoteAverage()));
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE, movie.getDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, favorite);
        return contentValues;
    }

    public void addFavorites(Movie movie) {
        ContentValues contentValues = setMovies(movie, "true");
        // update movie table

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, new Long(movie.getId()));

        getActivity().getContentResolver().update(uri, contentValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});
        ib.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fav_on));
        Toast.makeText(getActivity(), "Added ...", Toast.LENGTH_SHORT).show();
    }

    public void remFromFav(Movie movie) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, new Long(movie.getId()));
        ib.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fav_off));
        ContentValues contentValues = setMovies(movie, "false");
        getActivity().getContentResolver().update(uri, contentValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});
        Toast.makeText(getActivity(), "Removed ...", Toast.LENGTH_SHORT).show();
    }


    private ImageView img;
    private ImageButton ib;
    private LinearLayout trailersLayout, reviewsLayout;
    private ImageLoader imageLoader = null;
    public static final String className = MoviesDetailFragment.class.getSimpleName();

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.v(className, "In Ex : " + e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        imageLoader = VolleyController.getInstance(this.getActivity()).getImageLoader();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        trailersLayout = (LinearLayout) rootView.findViewById(R.id.layoutTrailers);
        reviewsLayout = (LinearLayout) rootView.findViewById(R.id.layoutReviews);
        Movie movtemp = new Movie();
        if (MainActivity.mTwoPane) {
            Log.v(className, "detail view : dual pane");
            Bundle arguments = getArguments();
            if (arguments != null) {
                movtemp = (Movie) arguments.getParcelable("movie");
                Log.v(className, "mov is :" + movtemp.toString());
            } else {

            }

        } else {
            Log.v(className, "detail view : normal pane");
            Bundle extras = getActivity().getIntent().getExtras();
            movtemp = (Movie) extras.getParcelable("movie");

        }
        final Movie movie = movtemp;
        Log.v(className, "Selected Movie : " + movie);
        if (movie != null) {
            movieId = movie.getId();
            if (movie.getId() != null) {
                TextView overview = (TextView) rootView.findViewById(R.id.overview);
                TextView title = (TextView) rootView.findViewById(R.id.title);
                TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
                Button rating = (Button) rootView.findViewById(R.id.rating);
                ib = (ImageButton) rootView.findViewById(R.id.imagefavouriteButton);

                overview.setText(movie.getOverview());
                title.setText(movie.getTitle());
                releaseDate.setText(movie.getDate());
                rating.setText(movie.getVoteAverage() + "/10");

                img = (ImageView) rootView.findViewById(R.id.image);
                String url = getString(R.string.detail_view_image_url) + movie.getPosterUrl();
                Log.v(className, "Values inserted into UI elements.");
                new DownloadImage().execute(url);

                // for favorite

                if (checkFavorite(movie.getId())) {
                    ib.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fav_on));
                } else {
                    ib.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.fav_off));
                }


                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkFavorite(movie.getId())) {
                            remFromFav(movie);
                        } else {
                            addFavorites(movie);
                        }
                    }
                });

                loadVideos(movie.getId());
                loadReviews(movie.getId());

                trailersLayout.setVisibility(View.GONE);
                reviewsLayout.setVisibility(View.GONE);

                TextView reviewHeader = (TextView) rootView.findViewById(R.id.review_heading);
                TextView trailerHeader = (TextView) rootView.findViewById(R.id.trailers_heading);

                reviewHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View viewIn) {
                        try {
                            toggle_review(viewIn);
                        } catch (Exception except) {
                            Log.v(className, "on click exception : " + except.getMessage());
                        }
                    }
                });

                trailerHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View viewIn) {
                        try {
                            toggle_trailer(viewIn);
                        } catch (Exception except) {
                            Log.v(className, "on click exception : " + except.getMessage());
                        }
                    }
                });

            }
        }
        return rootView;
    }


    private boolean checkFavorite(String movieId) {
        boolean result = false;
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, new Long(movieId));
        //Cursor cursor = getActivity().getContentResolver().query(uri, null, MovieContract.VideoEntry.COLUMN_MOVIE_ID + "=?", new String[]{movieId}, null);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " =? ", new String[]{movieId}, null);
        if (cursor != null && cursor.getCount() > 0) {
            String sFavorite = "";
            if (cursor.getCount() == 1) {
                int favoriteIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE);
                cursor.moveToFirst();
                sFavorite = cursor.getString(cursor.getColumnIndex("favorite"));
            } else {
                cursor.moveToFirst();
                int favoriteIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE);
                sFavorite = cursor.getString(favoriteIndex);
            }
            if (sFavorite.equalsIgnoreCase("true")) {
                result = true;
            }


            cursor.close();
        }
        return result;
    }

    public static void slide_down(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.startAnimation(a);
            }
        }
    }

    public void toggle_review(View v) {
        if (reviewsLayout.isShown()) {
            slide_up(this.getContext(), reviewsLayout);
            reviewsLayout.setVisibility(View.GONE);
        } else {
            reviewsLayout.setVisibility(View.VISIBLE);
            slide_down(this.getContext(), reviewsLayout);
        }
    }

    public void toggle_trailer(View v) {
        if (trailersLayout.isShown()) {
            slide_up(this.getContext(), trailersLayout);
            trailersLayout.setVisibility(View.GONE);
        } else {
            trailersLayout.setVisibility(View.VISIBLE);
            slide_down(this.getContext(), trailersLayout);
        }
    }

    public LinearLayout createReviewLayout(String author, final String content) {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        LinearLayout linearLayout = new LinearLayout(this.getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(10, 10, 10, 10);
        TextView textViewContent = new TextView(this.getActivity());
        textViewContent.setText("\" " + content + " \"");
        textViewContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textViewContent.setPadding(0, 10, 0, 10);
        textViewContent.setLayoutParams(layoutParams);
        textViewContent.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        linearLayout.addView(textViewContent);
        TextView textViewAuthor = new TextView(this.getActivity());
        textViewAuthor.setText("-" + author);
        textViewAuthor.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textViewAuthor.setPadding(0, 0, 20, 30);
        textViewAuthor.setLayoutParams(layoutParams);
        textViewAuthor.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        linearLayout.addView(textViewAuthor);
        return linearLayout;
    }


    public LinearLayout createTrailerLayout(String name, final String source) {
        LinearLayout linearLayout = new LinearLayout(this.getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setWeightSum(100f);
        linearLayout.setPadding(10, 10, 10, 10);
        AdjustableImageView adjustableImageView = new AdjustableImageView(this.getActivity());
        adjustableImageView.setAdjustViewBounds(true);
        adjustableImageView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 50f));
        adjustableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + source)));
            }
        });
        linearLayout.addView(adjustableImageView);
        TextView textView = new TextView(this.getActivity());
        textView.setText(name);
        textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        textView.setPadding(20, 0, 0, 0);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 50f);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
        linearLayout.addView(textView);
        imageLoader.get("http://img.youtube.com/vi/" + source + "/0.jpg", ImageLoader.getImageListener(adjustableImageView, R.drawable.loading_trailer, R.drawable.error_trailer));
        return linearLayout;
    }

    public View getLineView() {
        View view = new View(this.getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setBackgroundColor(Color.GRAY);
        view.setMinimumHeight(2);
        return view;
    }

    String movieId;


    public void loadVideos(String movieId1) {

        try {
            Uri uri = MovieContract.VideoEntry.CONTENT_URI;
            uri = ContentUris.withAppendedId(uri, new Long(movieId1));
            Cursor cursor = getActivity().getContentResolver().query(uri, null, MovieContract.VideoEntry.COLUMN_MOVIE_ID + "=?", new String[]{movieId1}, null);
            if (cursor.getCount() > 0) {
                try {
                    int count = 0;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String sName = cursor.getString(cursor.getColumnIndex("name"));
                        String sSource = cursor.getString(cursor.getColumnIndex("source"));
                        trailersLayout.addView(createTrailerLayout(sName, sSource));
                        if ((count + 1) != cursor.getCount()) {
                            trailersLayout.addView(getLineView());
                        }
                        count++;
                    }
                } finally {
                    cursor.close();
                }
            } else {

                Log.v(className, "no trailers conditions");
                TextView textView = new TextView(getContext());
                textView.setText("No trailers found");
                textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                textView.setPadding(10, 10, 10, 10);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(15f);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                trailersLayout.addView(textView);
            }
            System.out.println("video cursor : " + cursor.getCount());
        } catch (Exception ex) {
            System.out.println("ex : " + ex.getMessage());
        }
    }


    public void loadReviews(String id) {

        try {
            Uri uri = MovieContract.ReviewEntry.CONTENT_URI;
            uri = ContentUris.withAppendedId(uri, new Long(id));
            Cursor cursor = getActivity().getContentResolver().query(uri, null, MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null);
            if (cursor.getCount() > 0) {
                try {
                    int count = 0;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                        String sAuthor = cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
                        String sContent = cursor.getString(cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR));
                        reviewsLayout.addView(createReviewLayout(sAuthor, sContent));
                        if ((count + 1) != cursor.getCount()) {
                            reviewsLayout.addView(getLineView());
                        }
                        count++;                                 // do what you need with the cursor here
                    }
                } finally {
                    cursor.close();
                }
                System.out.println("review cursor : " + cursor.getCount());

            } else {
                Log.v(className, "no reviews conditions");
                TextView textView = new TextView(getContext());
                textView.setText("No Reviews found");
                textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                textView.setPadding(10, 10, 10, 10);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(15f);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                reviewsLayout.addView(textView);
            }
        } catch (Exception ex) {
            System.out.println("ex : " + ex.getMessage());
        }
    }


}