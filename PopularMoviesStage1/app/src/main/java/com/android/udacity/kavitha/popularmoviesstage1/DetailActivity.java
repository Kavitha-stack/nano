package com.android.udacity.kavitha.popularmoviesstage1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class DetailActivity extends AppCompatActivity {

    private String className = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.not_supported_action, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesDetailFragment())
                    .commit();
        }
        Log.v(className, "Added Movies detail fragment.");
    }


    public static class MoviesDetailFragment extends Fragment {

        ImageView img;
        //Bitmap bitmap;

        public static final String className = MoviesDetailFragment.class.getSimpleName();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Bundle extras = getActivity().getIntent().getExtras();
            Movie movie = (Movie)extras.getParcelable("movie");
            Log.v(className, "Selected Movie : " + movie.toString());
            if (movie != null) {
                TextView overview = (TextView) rootView.findViewById(R.id.overview);
                TextView title = (TextView) rootView.findViewById(R.id.title);
                TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
                Button rating = (Button) rootView.findViewById(R.id.rating);
                overview.setText(movie.getOverview());
                title.setText(movie.getTitle());
                releaseDate.setText(movie.getDate());
                rating.setText(movie.getVoteAverage());
                img = (ImageView) rootView.findViewById(R.id.image);
                String url = getString(R.string.detail_view_image_url) + movie.getPosterUrl();
                Log.v(className, "Values inserted into UI elements.");
                new DownloadImage().execute(url);
            }
            return rootView;
        }

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
    }

}
