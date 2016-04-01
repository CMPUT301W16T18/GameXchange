package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.Date;

/**
 * Created by Adam on 2016-03-25.
 */
public class Review {
    private Date date;
    private String reviewBody;
    private float rating;
    private String reviewer;
    private String gameID;

    public Review() {
        this.date = new Date();
        this.reviewBody = "";
        this.rating = 0.0F;
        this.reviewer = "";
        this.gameID = "";
    }

    public Review(Date date, String reviewer, String gameID, float rating, String reviewBody) {
        this.date = date;
        this.reviewBody = reviewBody;
        this.rating = rating;
        this.reviewer = reviewer;
        this.gameID = gameID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}
