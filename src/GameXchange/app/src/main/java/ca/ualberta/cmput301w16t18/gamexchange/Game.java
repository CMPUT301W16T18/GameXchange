package ca.ualberta.cmput301w16t18.gamexchange;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class Game implements Serializable {
    private String id;
    private String status;
    private String title;
    private String developer;
    private String platform;
    private ArrayList<String> genres;
    private String description;
    private String picture;
    private ArrayList<Bid> bids;

    public Game() {
        this.id = "";
        this.status = "";
        this.title = "";
        this.developer = "";
        this.platform = "";
        this.genres = new ArrayList<String>();
        this.description = "";
        this.picture = "";
        this.bids = new ArrayList<Bid>();
    }

    public Game(String id, String status, String title, String developer, String platform,
                ArrayList<String> genres, String description, String picture, ArrayList<Bid> bids) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.developer = developer;
        this.platform = platform;
        this.genres = genres;
        this.description = description;
        this.picture = picture;
        this.bids = bids;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }
}
