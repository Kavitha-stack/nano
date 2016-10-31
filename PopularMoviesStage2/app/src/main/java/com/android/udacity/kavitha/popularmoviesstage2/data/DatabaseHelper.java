package com.android.udacity.kavitha.popularmoviesstage2.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.udacity.kavitha.popularmoviesstage2.model.Movie;

import java.util.ArrayList;

/**
 * Created by Kavitha on 9/7/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String className = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "PopMovie02.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        clearDatabase(sqLiteDatabase);
        try {
            final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                    MovieContract.PATH_MOVIE + "(" + MovieContract.MovieEntry._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_FAVORITE + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_POPULARITY + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                    MovieContract.MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

            final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " +
                    MovieContract.PATH_VIDEO + "(" + MovieContract.VideoEntry._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.VideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    MovieContract.VideoEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                    MovieContract.VideoEntry.COLUMN_SIZE + " TEXT NOT NULL, " +
                    MovieContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);

            final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                    MovieContract.PATH_REVIEW + "(" + MovieContract.ReviewEntry._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                    MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                    MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);


        } catch (Exception ex) {
            Log.e(className, "create table ex : " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(className, "DB upgrade from version " + oldVersion + " to " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_MOVIE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_VIDEO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_REVIEW);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.PATH_MOVIE + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.PATH_VIDEO + "'");
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MovieContract.PATH_REVIEW + "'");
        onCreate(sqLiteDatabase);

    }


    public void clearDatabase(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_VIDEO);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_REVIEW);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.PATH_MOVIE);
        } catch (Exception ex) {
            Log.e(className, "Clear db ex : " + ex.getMessage());
        }
    }
}
