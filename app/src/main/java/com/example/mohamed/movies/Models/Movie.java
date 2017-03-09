package com.example.mohamed.movies.Models;

import java.io.Serializable;

/**
 * Created by Mohamed on 01/11/2016.
 */

public class Movie implements Serializable {

    final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String IMAGE_SMALL_SIZE = "w185";


    private int MovieId;
    private String Title;
    private String ImageURL;
    private String Overview;
    private String VoteAverage;
    private String ReleaseDate;

    public Movie() {
    }

    public Movie(int MovieId, String ImageURL, String Title, String ReleaseDate,
                 String VoteAverage, String Overview) {
        this.MovieId = MovieId;
        this.ImageURL = ImageURL;
        this.Title = Title;
        this.ReleaseDate = ReleaseDate;
        this.VoteAverage = VoteAverage;
        this.Overview = Overview;
    }

    public int getMovieId() {
        return MovieId;
    }

    public void setMovieId(int movieId) {
        MovieId = movieId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public String getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        VoteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getImageFullURL() {
        return IMAGE_BASE_URL + IMAGE_SMALL_SIZE + getImageURL();
    }


}
