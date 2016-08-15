package com.android.udacity.kavitha.popularmoviesstage1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kavitha on 8/3/2016.
 */
public class MoviesMainFragment extends Fragment {

    private final String className = MoviesMainFragment.class.getSimpleName();
    private CustomAdapter customAdapter;

    @Override
    public void onStart() {
        super.onStart();
        Log.v(className, "Inside onStart of moviesMainFragment.");
        loadImages();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, vg, false);
        try {
            GridView gridview = (GridView) rootView.findViewById(R.id.gridView_movies);
            Log.v(className, "Inside createView of moviesMainFragment.");
            customAdapter = new CustomAdapter(this.getActivity(), R.layout.list_item_movies, new ArrayList<Movie>());
            gridview.setAdapter(customAdapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Movie selectedMovie = customAdapter.getItem(position);
                    Intent i = new Intent(getContext(), DetailActivity.class);
                    i.putExtra("movie", selectedMovie);
                    startActivity(i);
                }
            });
        } catch (Exception ex) {
            Log.e(className, "Ex in oncreateView : " + ex.getMessage());
        }
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Log.v(className, "In refresh option.");
                loadImages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadImages() {
        try {
            ImageDownloadTask idt = new ImageDownloadTask();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOption = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
            Log.v(className, "Sort option is : " + sortOption);
            idt.execute(sortOption);
        } catch (Exception ex) {
            Log.e(className, "In loadImage Ex : :" + ex.getMessage());
        }
    }


    public class ImageDownloadTask extends AsyncTask<String, Void, List<Movie>> {

        private final String className = ImageDownloadTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... params) {

            try {
                Log.v(className, "Background task started .");
                return connectNetwork(params);
            } catch (Exception e) {
                Log.e(className, "In doInBackground Ex : " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> lstMovies) {
            customAdapter.clear();
            Log.v(className, "image adapter cleared.");
            if (lstMovies != null) {
                for (Movie m : lstMovies) {
                    customAdapter.add(m);
                }
                Log.v(className, "Added Movies and Post execute completed...");
            }
        }


        public List<Movie> connectNetwork(String[] params) throws JSONException {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            String movieOption = params[0];
            Log.v(className, "params:" + movieOption);

            try {

                String movieBaseUrl = getString(R.string.movie_base_url);
                if (movieOption.equalsIgnoreCase(getString(R.string.pref_sort_default))) {
                    movieBaseUrl += getString(R.string.popular);
                } else {
                    movieBaseUrl += getString(R.string.top_rated);
                }
                String queryParam = getString(R.string.api_key);

                Uri builtUri = Uri.parse(movieBaseUrl).buildUpon()
                        .appendQueryParameter(queryParam, getString(R.string.api_value))
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(className, "URL : " + movieBaseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(className, " IO Ex : " + e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(className, "IO Ex in reader close : " + e);
                    }
                }
            }
            try {
                return getMovieData(movieJsonStr);
            } catch (Exception e) {
                Log.e(className, "Ex : " + e);
            }
            return null;
        }


        public List<Movie> getMovieData(String jsonStr) {

            List<Movie> lstMovies = new ArrayList<>();
            try {
                final String LIST = "results";
                final String OVERVIEW = "overview";
                final String ID = "id";
                final String TITLE = "title";
                final String VOTE_AVERAGE = "vote_average";
                final String BACKDROP_PATH = "backdrop_path";
                final String POSTER_PATH = "poster_path";
                final String RELEASE_DATE = "release_date";

                JSONObject movieJson = new JSONObject(jsonStr);
                JSONArray movieArray = movieJson.getJSONArray(LIST);

                for (int i = 0; i < movieArray.length(); i++) {

                    JSONObject eachMovie = movieArray.getJSONObject(i);
                    String overview = eachMovie.getString(OVERVIEW);
                    String id = eachMovie.getString(ID);
                    String title = eachMovie.getString(TITLE);
                    String voteAvg = eachMovie.getString(VOTE_AVERAGE);
                    String backdropPath = eachMovie.getString(BACKDROP_PATH);
                    String posterPath = eachMovie.getString(POSTER_PATH);
                    String releaseDate = eachMovie.getString(RELEASE_DATE);
                    Movie m = new Movie();
                    m.setId(id);
                    m.setTitle(title);
                    m.setOverview(overview);
                    m.setVoteAverage(voteAvg);
                    m.setBackdropUrl(backdropPath);
                    m.setPosterUrl(posterPath);
                    m.setDate(releaseDate);
                    lstMovies.add(m);
                }
            } catch (Exception ex) {
                Log.e(className, "Ex in getMovieDetails:" + ex.getMessage());
            }
            return lstMovies;
        }
    }

}
