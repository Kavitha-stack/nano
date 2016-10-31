package com.android.udacity.kavitha.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kavitha on 9/20/2016.
 */

public final class Review implements Parcelable {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String id;
    private String movieId;
    private String author;
    private String content;
    private String url;

    public Review() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.movieId);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);

    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.movieId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();

    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public static final class Response {

        public long id;
        public int page;
        public List<Review> reviews;
        public int totalPages;
        public int totalResults;
    }
}
