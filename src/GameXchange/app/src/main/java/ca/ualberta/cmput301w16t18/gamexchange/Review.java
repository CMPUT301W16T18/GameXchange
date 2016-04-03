package ca.ualberta.cmput301w16t18.gamexchange;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Adam on 2016-03-25.
 */
public class Review {
    private long timestamp; //milliseconds since UNIX epoch
    private String reviewBody;
    private float rating;
    private String reviewer;
    private String gameID;

    public Review() {
        this.timestamp = 0;
        this.reviewBody = "";
        this.rating = 0.0F;
        this.reviewer = "";
        this.gameID = "";
    }

    public Review(long timestamp, String reviewBody, float rating, String reviewer, String gameID) {
        this.timestamp = timestamp;
        this.reviewBody = reviewBody;
        this.rating = rating;
        this.reviewer = reviewer;
        this.gameID = gameID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return new Date(timestamp);
    }

    public void setDate(Date date) {
        this.timestamp = date.getTime();
    }

    public String getDateString() {
        DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
        return dateTimeInstance.format(getDate());
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

    public void setRating(float rating) {
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
