package com.example.mohamed.movies.Models;

import java.io.Serializable;

/**
 * Created by Mohamed on 25/11/2016.
 */

public class Trailers implements Serializable {

    private String trailerKey;
    private String trailerName;
    private String trailerID;

    public Trailers() {

    }

    public Trailers(String trailerID, String trailerName, String trailerKey) {
        this.trailerID = trailerID;
        this.trailerName = trailerName;
        this.trailerKey = trailerKey;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

}
