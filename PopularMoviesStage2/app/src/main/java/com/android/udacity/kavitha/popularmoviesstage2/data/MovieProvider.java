package com.android.udacity.kavitha.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.udacity.kavitha.popularmoviesstage2.activities.MainActivity;

/**
 * Created by Kavith on 9/7/2016.
 */
public class MovieProvider extends ContentProvider {

    private DatabaseHelper dbHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 200;
    private static final int VIDEO = 300;
    private static final int VIDEO_WITH_ID = 400;
    private static final int REVIEW = 500;
    private static final int REVIEW_WITH_ID = 600;


    private static final String className = MovieProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/#", VIDEO_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor = null;
        try {
            System.out.println("Query Param : " + uri.toString() + ";projection : " + projection + ";selectionArgs:" + selectionArgs + ";sort order :" + sortOrder);
            switch (uriMatcher.match(uri)) {
                case MOVIE: {
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_MOVIE,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    return retCursor;
                }
                case MOVIE_WITH_ID: {
                    String id = String.valueOf(ContentUris.parseId(uri));
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_MOVIE,
                            projection,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{id},
                            null,
                            null,
                            sortOrder);

                    return retCursor;
                }
                case VIDEO: {
                    String id = String.valueOf(ContentUris.parseId(uri));
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_VIDEO,
                            projection,
                            MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{id},
                            null,
                            null,
                            sortOrder);
                    return retCursor;
                }
                case VIDEO_WITH_ID: {
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_VIDEO,
                            projection,
                            MovieContract.VideoEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(ContentUris.parseId(uri))},
                            null,
                            null,
                            sortOrder);


                    return retCursor;
                }
                case REVIEW: {
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_REVIEW,
                            projection,
                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(ContentUris.parseId(uri))},
                            null,
                            null,
                            sortOrder);


                    return retCursor;
                }
                case REVIEW_WITH_ID: {
                    retCursor = dbHelper.getReadableDatabase().query(
                            MovieContract.PATH_REVIEW,
                            projection,
                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(ContentUris.parseId(uri))},
                            null,
                            null,
                            sortOrder);
                    return retCursor;
                }
                default: {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }
        } catch (Exception ex) {
            Log.v(className, "query exception:" + ex.getMessage());
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                return MovieContract.MovieEntry.CONTENT_TYPE;
            }
            case MOVIE_WITH_ID: {
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            case VIDEO: {
                return MovieContract.VideoEntry.CONTENT_TYPE;
            }
            case VIDEO_WITH_ID: {
                return MovieContract.VideoEntry.CONTENT_ITEM_TYPE;
            }
            case REVIEW: {
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            }
            case REVIEW_WITH_ID: {
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        System.out.println("inside single insert");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                System.out.println("inside movie of insert in provider:" + contentValues.toString());
                // long did = db.delete(MovieContract.PATH_MOVIE, null, contentValues);
                long _id = db.insert(MovieContract.PATH_MOVIE, null, contentValues);
                System.out.println("iid:" + _id);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    System.out.println("greateer id");
                    returnUri = MovieContract.MovieEntry.buildMoviesUri(_id);
                } else {
                    System.out.println("else.....");
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case VIDEO: {
                System.out.println("inside video of insert in provider:" + contentValues.toString());
                long _id = db.insert(MovieContract.PATH_VIDEO, null, contentValues);
                System.out.println("iid:" + _id);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    System.out.println("greateer id");
                    returnUri = MovieContract.VideoEntry.buildVideosUri(_id);
                } else {
                    System.out.println("else.....");
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case REVIEW: {
                System.out.println("inside review of insert in provider:" + contentValues.toString());
                long _id = db.insert(MovieContract.PATH_REVIEW, null, contentValues);
                System.out.println("iid:" + _id);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    System.out.println("greateer id");
                    returnUri = MovieContract.ReviewEntry.buildReviewsUri(_id);
                } else {
                    System.out.println("else.....");
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case MOVIE:
                numDeleted = db.delete(
                        MovieContract.PATH_MOVIE, selection, selectionArgs);
                // reset _ID
               /* db.execSQL("DELETE FROM MOVIE WHERE NAME = '" +
                        MovieContract.PATH_MOVIE + "'");*/
                break;
            case MOVIE_WITH_ID:
                numDeleted = db.delete(MovieContract.PATH_MOVIE,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});

                // reset _ID
               /* db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE COLUMN_MOVIE_ID = '" +
                        String.valueOf(ContentUris.parseId(uri)) + "'");*/

                break;
            case VIDEO_WITH_ID:
                numDeleted = db.delete(MovieContract.PATH_VIDEO,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
               /* db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.PATH_VIDEO + "'");*/

                break;
            case REVIEW_WITH_ID:
                numDeleted = db.delete(MovieContract.PATH_REVIEW,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
               /* db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.PATH_REVIEW + "'");*/

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return numDeleted;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        // System.out.println("match:"+match);
        switch (match) {
            case MOVIE:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insert(MovieContract.PATH_MOVIE, null, value);
                            //_id = db.insertWithOnConflict(MovieContract.PATH_MOVIE, null, value, SQLiteDatabase.CONFLICT_IGNORE );
                        } catch (SQLiteConstraintException e) {
                            Log.w(className, "Attempting to insert " +
                                    value.getAsString(
                                            MovieContract.MovieEntry.COLUMN_TITLE)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once

                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion , notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;

            case VIDEO:
                // System.out.println("VIDEO _WITH_ID");
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        //   System.out.println("video blk insert Values : "+value);
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insert(MovieContract.PATH_VIDEO,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(className, "Attempting to insert " +
                                    value.getAsString(
                                            MovieContract.VideoEntry.COLUMN_NAME)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // System.out.println("success : "+numInserted);
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;

            case REVIEW:
                // System.out.println("REVIEW_WITH_ID");
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        // System.out.println("Review blk insert Values : "+value);
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insert(MovieContract.PATH_REVIEW,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(className, "Attempting to insert " +
                                    value.getAsString(
                                            MovieContract.ReviewEntry.COLUMN_MOVIE_ID)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // System.out.println("success : "+numInserted);
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;

            default: {
                return super.bulkInsert(uri, values);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // Log.v(className, "inside update.");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numUpdated = 0;
        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (uriMatcher.match(uri)) {
            case MOVIE: {
                // System.out.println("inside update m:");
                numUpdated = db.update(MovieContract.PATH_MOVIE,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {

                String id = String.valueOf(ContentUris.parseId(uri));
                // System.out.println("inside update m w id:"+id);
                numUpdated = db.update(MovieContract.PATH_MOVIE,
                        contentValues,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{id});
                // System.out.println("numUpdated:"+numUpdated);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}
