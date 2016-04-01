package ca.ualberta.cmput301w16t18.gamexchange;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Adam on 2016-03-21.
 */
public class Bid {

    private String bidder;
    private double price;
    private LatLng location;
    private String status;

    public Bid() {
        this.bidder = "";
        this.price = 0.0;
        this.location = new LatLng(0.0, 0.0);
        this.status = Constants.PENDING;
    }

    public Bid(String bidder, double price, LatLng location) {
        this.bidder = bidder;
        this.price = price;
        this.location = location;
        this.status = Constants.PENDING;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getMapsString() {
        // from http://stackoverflow.com/questions/3990110/how-to-show-marker-in-maps-launched-by-geo-uri-intent
        return "geo:" + location.latitude + "," + location.longitude + "?z=10&q=" + location.latitude + "," + location.longitude + "(Trade location)";
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

    public void reject() {
        this.status = Constants.REJECTED;
    }
}
