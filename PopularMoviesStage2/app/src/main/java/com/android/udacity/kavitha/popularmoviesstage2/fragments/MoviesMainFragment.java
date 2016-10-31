package com.android.udacity.kavitha.popularmoviesstage2.fragments;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.android.udacity.kavitha.popularmoviesstage2.activities.DetailActivity;
import com.android.udacity.kavitha.popularmoviesstage2.R;
import com.android.udacity.kavitha.popularmoviesstage2.activities.MainActivity;
import com.android.udacity.kavitha.popularmoviesstage2.data.MovieContract;
import com.android.udacity.kavitha.popularmoviesstage2.model.Movie;
import com.android.udacity.kavitha.popularmoviesstage2.model.Review;
import com.android.udacity.kavitha.popularmoviesstage2.model.Video;
import com.android.udacity.kavitha.popularmoviesstage2.service.VolleyController;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kavitha on 8/3/2016.
 */
public class MoviesMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String className = MoviesMainFragment.class.getSimpleName();

    private MovieAdapter moviesAdapter;
    private FragmentManager fm;

    public MoviesMainFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Cursor c = null;
        String strType = getSortOption();
        try {
            Log.v(className, "Type : " + strType);
            if (strType.equalsIgnoreCase(getString(R.string.popular))) {
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{strType}, null);
                Log.v(className, "Checking popular data : " + c.getCount());
                if (c == null || c.getCount() == 0) {
                    loadData(strType);
                }
            } else if (strType.equalsIgnoreCase(getString(R.string.top_rated))) {
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{strType}, null);
                Log.v(className, "Checking toprated data : " + c.getCount());
                if (c == null || c.getCount() == 0) {
                    loadData(strType);
                }
            } else if (strType.equalsIgnoreCase(getString(R.string.my_favorite))) {
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "favorite=?", new String[]{"true"}, null);
                Log.v(className, "checking my favorite : " + strType);
                if (c == null || c.getCount() == 0) {
                    Toast.makeText(getContext(), "No movies in your favorites.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Operation not supported.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e(className, "on start ex : " + ex.getMessage());
        }
    }

    private boolean loadData(String type) {
        boolean result = false;
        Toast.makeText(getContext(), "Data sync is in progress.", Toast.LENGTH_SHORT).show();
        try {
            if (isConnected()) {
                if (type.equalsIgnoreCase("all")) {
                    String[] optionList = new String[]{getString(R.string.popular), getString(R.string.top_rated)};
                    for (String s : optionList) {
                        new FetchMoviesTask().execute(type);
                    }
                } else {
                    new FetchMoviesTask().execute(type);
                }

                result = true;
            } else {
                Toast.makeText(getContext(), "Network not available.", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getContext(), "Data sync completed.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.e(className, "In loadData Ex : :" + ex.getMessage());
            result = false;
        }
        return result;
    }


    private String getSortOption() {
        String type = "";
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOption = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

            if (sortOption.equalsIgnoreCase(getString(R.string.pref_sort_default))) {
                type = getString(R.string.popular);
            } else if (sortOption.equalsIgnoreCase(getString(R.string.pref_sort_top))) {
                type = getString(R.string.top_rated);
            } else if (sortOption.equalsIgnoreCase(getString(R.string.pref_sort_favorite))) {
                type = getString(R.string.my_favorite);
            } else {
                type = getString(R.string.popular);
            }
        } catch (Exception ex) {
            Log.e(className, "getSortOption ex: " + ex.getMessage());
        }
        return type;
    }

    public boolean isConnected() {
        boolean result = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            result = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception ex) {
            result = false;
            Log.v(className, "Ex of isConnected : " + ex.getMessage());
        }
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       /* Cursor c = null;
        String strType = getSortOption();
        try {
            Log.v(className, "Type : " + strType);
            if (strType.equalsIgnoreCase(getString(R.string.popular))){
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{strType}, null);
                Log.v(className, "Checking popular data : "+c.getCount());
                if (c == null || c.getCount() == 0) {
                   loadData(strType);
                }
            } else if (strType.equalsIgnoreCase(getString(R.string.top_rated))){
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{strType}, null);
                Log.v(className, "Checking toprated data : "+c.getCount());
                if (c == null || c.getCount() == 0) {
                   loadData(strType);
                }
            }else if (strType.equalsIgnoreCase(getString(R.string.my_favorite))){
                c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "favorite=?", new String[]{"true"}, null);
                Log.v(className, "checking my favorite : " + strType);
                if (c == null || c.getCount() == 0) {
                    Toast.makeText(getContext(), "No movies in your favorites.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getContext(), "Operation not supported.", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Log.e(className, "on onActivityCreated ex : "+ex.getMessage());
        }*/
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, vg, false);

        try {
            GridView gridview = (GridView) rootView.findViewById(R.id.gridView_movies);
            moviesAdapter = new MovieAdapter(getActivity(), null, 0, 0);
            moviesAdapter.notifyDataSetChanged();
            gridview.setAdapter(moviesAdapter);
            gridview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Cursor cMovie = (Cursor) moviesAdapter.getItem(position);
                    Movie m = getMovie(cMovie);
                    Toast.makeText(getContext(), m.getTitle() + " selected.", Toast.LENGTH_SHORT).show();

                    //  MainActivity.APP_START_TAG = "STARTED";
                    if (MainActivity.mTwoPane) {
                        fm = getActivity().getSupportFragmentManager();
                        Log.v(className, "inside dual pane.");
                        Bundle b1 = new Bundle();
                        b1.putParcelable("movie", m);
                        FragmentTransaction ft = fm.beginTransaction();
                        MoviesDetailFragment mdf = new MoviesDetailFragment();
                        mdf.setArguments(b1);
                        ft.replace(R.id.container, mdf, "DFTAG");
                        ft.commit();
                    } else {
                        Log.v(className, "inside normal pane.");
                        Intent i = new Intent(getContext(), DetailActivity.class);
                        i.putExtra("movie", m);
                        startActivity(i);
                    }
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
                //fetching all(popular and top rated) the values from movies api and pushing to db.
                loadData("all");
                return true;
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String strType = getSortOption();
        if (strType.equalsIgnoreCase(getString(R.string.my_favorite))) {
            return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, "favorite=?", new String[]{"true"}, null);
        } else {
            return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{strType}, null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(className, "onloadfinished cursor : " + data.getCount());
        moviesAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }

    private Movie getMovie(Cursor cMovie) {
        Movie m = new Movie();
        int movieIdIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        final String movieId = cMovie.getString(movieIdIndex);
        m.setId(movieId);

        int imageIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP);
        String imageUrl = cMovie.getString(imageIndex);

        String backUrls = getContext().getString(R.string.main_view_image_url) + imageUrl;
        m.setBackdropUrl(backUrls);

        int titleIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        final String title = cMovie.getString(titleIndex);
        m.setTitle(title);

        int ratingIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        final String rating = cMovie.getString(ratingIndex);
        m.setVoteAverage(rating);

        int overviewIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        final String overview = cMovie.getString(overviewIndex);
        m.setOverview(overview);

        int releaseIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE);
        final String release = cMovie.getString(releaseIndex);
        m.setDate(release);

        int favoriteIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE);
        final String favorite = cMovie.getString(favoriteIndex);
        m.setFavorite(favorite);

        int typeIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_TYPE);
        final String type = cMovie.getString(typeIndex);
        m.setType(type);

        int popularIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY);
        final String popular = cMovie.getString(popularIndex);
        m.setPopularity(popular);

        int posterIndex = cMovie.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        String posterUrl = cMovie.getString(posterIndex);

        String posterUrls = getContext().getString(R.string.main_view_image_url) + posterUrl;
        m.setPosterUrl(posterUrls);
        return m;

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Cursor> {

        private final String className = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Cursor doInBackground(String... params) {
            Cursor c = null;
            try {
                String movieOption = params[0];
                Log.v(className, "Background task started for all:" + movieOption);
                if (movieOption.equalsIgnoreCase("all")) {
                    String[] optionList = new String[]{getString(R.string.popular), getString(R.string.top_rated)};
                    for (String s : optionList) {
                        String[] types = new String[]{s};
                        c = connectNetwork(types);
                    }
                } else {
                    Log.v(className, "Background task started for " + movieOption);
                    c = connectNetwork(params);
                }
            } catch (Exception e) {
                Log.e(className, "In doInBackground Ex : " + e.getMessage());
            }
            return c;
        }

        @Override
        protected void onPostExecute(Cursor c) {
            moviesAdapter.swapCursor(c);
            MainActivity.APP_START_TAG = "STARTED";
        }


        public Cursor connectNetwork(String[] params) throws JSONException {
            // String type = "popular";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            String movieOption = params[0];
            try {
                String movieBaseUrl = getString(R.string.movie_base_url);
                if (movieOption.equalsIgnoreCase("popular")) {
                    movieBaseUrl += getString(R.string.popular);
                    //type = "popular";
                } else if (movieOption.equalsIgnoreCase("top_rated")) {
                    movieBaseUrl += getString(R.string.top_rated);
                    // type = "top_rated";
                } else {
                    //  type = getString(R.string.popular);
                    movieBaseUrl += getString(R.string.popular);
                }


                String queryParam = getString(R.string.api_key);

                Uri builtUri = Uri.parse(movieBaseUrl).buildUpon()
                        .appendQueryParameter(queryParam, getString(R.string.api_value))
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(className, "URL : " + url.toString());
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

                JSONObject movieJson = new JSONObject(movieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray("results");
                return getMovieData1(movieArray, movieOption);
            } catch (Exception e) {
                Log.e(className, "Ex : " + e);
            }
            return null;
        }

        private Cursor getMovieData1(JSONArray movieArray, String movieType) {
            boolean result = false;
            Cursor c = null;
            try {
                final String LIST = "results";
                final String OVERVIEW = "overview";
                final String ID = "id";
                final String TITLE = "title";
                final String VOTE_AVERAGE = "vote_average";
                final String BACKDROP_PATH = "backdrop_path";
                final String POSTER_PATH = "poster_path";
                final String RELEASE_DATE = "release_date";
                final String POPULARITY = "popularity";
                ContentValues[] moviesArr = new ContentValues[movieArray.length()];
                for (int i = 0; i < movieArray.length(); i++) {

                    JSONObject eachMovie = movieArray.getJSONObject(i);
                    String overview = eachMovie.getString(OVERVIEW);
                    String id = eachMovie.getString(ID);
                    String title = eachMovie.getString(TITLE);
                    String voteAvg = eachMovie.getString(VOTE_AVERAGE);
                    String backdropPath = eachMovie.getString(BACKDROP_PATH);
                    String posterPath = eachMovie.getString(POSTER_PATH);
                    String releaseDate = eachMovie.getString(RELEASE_DATE);
                    String popularity = eachMovie.getString(POPULARITY);

                    moviesArr[i] = new ContentValues();

                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_RATING, voteAvg);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_BACKDROP, backdropPath);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_POSTER, posterPath);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_RELEASE, releaseDate);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_TYPE, movieType);
                    moviesArr[i].put(MovieContract.MovieEntry.COLUMN_FAVORITE, "false");

                    if (MainActivity.APP_START_TAG.equalsIgnoreCase("started")) {
                        //       Log.v(className, "push videos to db delete:" + MainActivity.APP_START_TAG);
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                        uri = ContentUris.withAppendedId(uri, new Long(id));
                        getActivity().getContentResolver().delete(uri, null, null);
                    }

                    try {
                        loadVideos(id);
                        loadReviews(id);
                    } catch (Exception ex) {
                        Log.e(className, "error of video and review : " + ex.getMessage());
                    }

                }

                getActivity().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, moviesArr);

                Log.v(className, "Type : " + movieType);
                if (movieType.equalsIgnoreCase(getString(R.string.popular))) {
                    c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{movieType}, null);

                } else if (movieType.equalsIgnoreCase(getString(R.string.top_rated))) {
                    Log.v(className, "else : " + movieType);
                    c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "type=?", new String[]{movieType}, null);

                }
                // Toast.makeText(getContext(), "Data sync completed.", Toast.LENGTH_SHORT).show();
                // return c;


            } catch (Exception ex) {
                Log.e(className, "Ex in getMovieDetails:" + ex.getMessage());
                result = false;
            }
            return c;
        }


        protected boolean pushVideoToDB(List<Video> lstVideo) {
            try {
                ContentValues[] videoArr = new ContentValues[lstVideo.size()];
                for (int i = 0; i < lstVideo.size(); i++) {
                    videoArr[i] = new ContentValues();
                    videoArr[i].put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, lstVideo.get(i).getMovieId());
                    videoArr[i].put(MovieContract.VideoEntry.COLUMN_NAME, lstVideo.get(i).getName());
                    videoArr[i].put(MovieContract.VideoEntry.COLUMN_SOURCE, lstVideo.get(i).getSource());
                    videoArr[i].put(MovieContract.VideoEntry.COLUMN_SIZE, lstVideo.get(i).getSize());
                    videoArr[i].put(MovieContract.VideoEntry.COLUMN_TYPE, lstVideo.get(i).getType());

                    if (MainActivity.APP_START_TAG.equalsIgnoreCase("started")) {
                        //    Log.v(className,"push videos to db delete:"+MainActivity.APP_START_TAG);
                        Uri uri = MovieContract.VideoEntry.CONTENT_URI;
                        uri = ContentUris.withAppendedId(uri, new Long(lstVideo.get(i).getMovieId()));
                        getActivity().getContentResolver().delete(uri, null, null);
                    }
                }
                getActivity().getContentResolver().bulkInsert(MovieContract.VideoEntry.CONTENT_URI, videoArr);
                return true;
            } catch (Exception ex) {
                Log.e(className, "ex in pushVideo to db : " + ex.getMessage());
            }
            return false;
        }

        protected boolean pushReviewsToDB(List<Review> lstReview) {
            try {

                ContentValues[] reviewArr = new ContentValues[lstReview.size()];
                for (int i = 0; i < lstReview.size(); i++) {
                    reviewArr[i] = new ContentValues();
                    reviewArr[i].put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, lstReview.get(i).getMovieId());
                    reviewArr[i].put(MovieContract.ReviewEntry.COLUMN_AUTHOR, lstReview.get(i).getAuthor());
                    reviewArr[i].put(MovieContract.ReviewEntry.COLUMN_CONTENT, lstReview.get(i).getContent());
                    reviewArr[i].put(MovieContract.ReviewEntry.COLUMN_URL, lstReview.get(i).getUrl());
                    if (MainActivity.APP_START_TAG.equalsIgnoreCase("started")) {
                        //  Log.v(className,"push reviews to db delete:"+MainActivity.APP_START_TAG);
                        Uri uri = MovieContract.ReviewEntry.CONTENT_URI;
                        uri = ContentUris.withAppendedId(uri, new Long(lstReview.get(i).getMovieId()));
                        getActivity().getContentResolver().delete(uri, null, null);
                    }

                }
                getActivity().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewArr);
                return true;
            } catch (Exception ex) {
                Log.e(className, "ex in pushreview to db : " + ex.getMessage());
            }
            return false;
        }


        public void loadVideos(final String movieId) {
            String movieBaseUrl = getContext().getString(R.string.movie_base_url);
            String urls = movieBaseUrl + movieId + "/trailers?api_key=" + getContext().getString(R.string.api_value);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urls,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //findViewById(R.id.loadingIndicatorViewTrailers).setVisibility(View.GONE);
                            try {
                                String strId = response.getString("id");
                                JSONArray jsonArray = response.getJSONArray("youtube");
                                List<Video> lst = new ArrayList<Video>();
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String name = jsonObject.getString("name");
                                        String source = jsonObject.getString("source");
                                        String size = jsonObject.getString("size");
                                        String type = jsonObject.getString("type");
                                        Video v = new Video();
                                        v.setMovieId(movieId);
                                        v.setName(name);
                                        v.setSource(source);
                                        v.setSize(size);
                                        v.setType(type);
                                        lst.add(v);
                                    }
                                    pushVideoToDB(lst);
                                } else {
                                    Log.v(className, "no trailers conditions");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v(className, "on error of video");
                }
            });
            VolleyController.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        }

        public void loadReviews(String id) {

            String movieBaseUrl = getContext().getString(R.string.movie_base_url);
            String urls = movieBaseUrl + id + "/reviews?api_key=" + getContext().getString(R.string.api_value);
            JsonObjectRequest jsonObjectRequestReviews = new JsonObjectRequest(urls,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String strId = response.getString("id");
                                List<Review> lst = new ArrayList<Review>();
                                JSONArray jsonArray = response.getJSONArray("results");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String content = jsonObject.getString("content");
                                        String author = jsonObject.getString("author");
                                        String id = jsonObject.getString("id");
                                        String url = jsonObject.getString("url");
                                        Review r = new Review();
                                        r.setMovieId(strId);
                                        r.setContent(content);
                                        r.setAuthor(author);
                                        r.setUrl(url);
                                        lst.add(r);
                                    }
                                    //Log.v(className, "before pushing review to db");
                                    pushReviewsToDB(lst);
                                } else {
                                    Log.v(className, "no review conditions");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.v(className, "on error of review");
                }
            });
            VolleyController.getInstance(getContext()).addToRequestQueue(jsonObjectRequestReviews);
        }


    }
}