package com.example.android.popular_movies;


import java.io.Serializable;


public class Movie implements Serializable {

    private String backdrop_path;
    private int id;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private double popularity;
    private String title;
    private  String vote_average;
    private String vote_count;
    private String runtime;




    public String getVote_average() {
        return vote_average;
    }
    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }


    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
