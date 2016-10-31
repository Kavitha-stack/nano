package com.android.udacity.kavitha.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.util.List;

/**
 * Created by Kavitha on 8/6/2016.
 */
public class Movie implements Parcelable {

    private String id;
    private String title;
    private String overview;
    private String voteAverage;
    private String backdropUrl;
    private String posterUrl;
    private String date;
    private String favorite;
    private String type;
    private String popularity;

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", backdropUrl='" + backdropUrl + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", favorite='" + favorite + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", popularity='" + popularity + '\'' +
                '}';
    }

    public Movie() {
    }

    // We reconstruct the object reading from the Parcel data
    public Movie(Parcel p) {
        id = p.readString();
        title = p.readString();
        overview = p.readString();
        voteAverage = p.readString();
        backdropUrl = p.readString();
        posterUrl = p.readString();
        date = p.readString();
        favorite = p.readString();
        type = p.readString();
        popularity = p.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(backdropUrl);
        dest.writeString(posterUrl);
        dest.writeString(date);

        dest.writeString(favorite);
        dest.writeString(type);
        dest.writeString(popularity);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;//normalizeDate(Long.valueOf(date));
    }

/*
    public static String normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        String s =String.valueOf(time.setJulianDay(julianDay));
        System.out.println("time values are :"+s);
        return s;
    }*/

}
