package ca.ualberta.cmput301w16t18.gamexchange;

import com.google.android.gms.maps.model.LatLng;

/**
 * This class is the model for a bid and creates a new bid when called
 * A bid consists of bidder, price, latitude, longitude, and status
 * Bids are used within the gameView activity
 * This class also consists of many getters and setters
 */
class Bid {

    private String bidder;
    private double price;
    private double latitude;
    private double longitude;
    private String status;


    public Bid() {
        this.bidder = "";
        this.price = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.status = Constants.PENDING;
    }

    public Bid(String bidder, double price, double latitude, double longitude) {
        this.bidder = bidder;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = Constants.PENDING;
    }

    public Bid(String bidder, double price, double latitude, double longitude, String status) {
        this.bidder = bidder;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLocation() {
        return new LatLng(this.latitude, this.longitude);
    }

    public void setLocation(LatLng location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public String getMapsString() {
        // from http://stackoverflow.com/questions/3990110/how-to-show-marker-in-maps-launched-by-geo-uri-intent
        return "geo:" + latitude + "," + longitude + "?z=10&q=" + latitude + "," + longitude + "(Trade location)";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void accept() {
        this.status = Constants.ACCEPTED;
    }


}
