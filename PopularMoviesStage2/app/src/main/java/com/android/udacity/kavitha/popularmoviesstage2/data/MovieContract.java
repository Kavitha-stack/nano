package com.android.udacity.kavitha.popularmoviesstage2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Kavitha on 9/7/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.android.udacity.kavitha.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String _ID = "_id";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FAVORITE = "favorite";

        // for building URIs on insertion
        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";

        // for building URIs on insertion
        public static Uri buildVideosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String _ID = "_id";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";

        // for building URIs on insertion
        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
