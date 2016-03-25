package ca.ualberta.cmput301w16t18.gamexchange;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Adam on 2016-03-21.
 */
public class Bid {

    private String bidder;
    private double price;
    private LatLng location;

    public Bid(String bidder, double price, LatLng location) {
        this.bidder = bidder;
        this.price = price;
        this.location = location;
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

    public LatLng getPoint() {
        return location;
    }

    public void setPoint(LatLng point) {
        this.location = point;
    }
}
