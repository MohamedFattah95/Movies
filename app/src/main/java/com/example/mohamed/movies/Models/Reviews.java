package com.example.mohamed.movies.Models;

import java.io.Serializable;

/**
 * Created by Mohamed on 25/11/2016.
 */

public class Reviews implements Serializable {

    String reviewID;
    String reviewAuthor;
    String reviewContent;

    public Reviews() {
    }

    public Reviews(String reviewID, String reviewAuthor, String reviewContent) {
        this.reviewID = reviewID;
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewID() {
        return reviewID;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

}
