package com.android.udacity.kavitha.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kavitha on 9/20/2016.
 */
public final class Video implements Parcelable {

    private String id;
    private String movieId;
    private String name;
    private String source;
    private String size;
    private String type;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Video() {
    }


    // --------------------------------------------------------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.movieId);
        dest.writeString(this.name);
        dest.writeString(this.source);
        dest.writeString(this.size);
        dest.writeString(this.type);
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.movieId = in.readString();
        this.name = in.readString();
        this.source = in.readString();
        this.size = in.readString();
        this.type = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public static final class Response {


        public long id;

        public List<Video> videos;

    }
}
