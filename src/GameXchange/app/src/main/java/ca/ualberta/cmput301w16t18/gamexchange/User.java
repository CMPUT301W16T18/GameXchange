package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class User {
    private String id;
    private String email;
    private String name;
    private String passhash;
    private String address1;
    private String address2;
    private String city;
    private String phone;
    private String postal;
    private ArrayList<String> owned_games;
    private ArrayList<String> watchlist;
    private ArrayList<Review> reviews;

    public User(String id, String email, String name, String passhash, String address1,
                String address2, String city, String phone, String postal,
                ArrayList<String> owned_games, ArrayList<String> watchlist) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.passhash = passhash;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.phone = phone;
        this.postal = postal;
        this.owned_games = owned_games;
        this.watchlist = watchlist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasshash() {
        return passhash;
    }

    public void setPasshash(String passhash) {
        this.passhash = passhash;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public ArrayList<String> getGames() {
        return owned_games;
    }

    public void setGames(ArrayList<String> owned_games) {
        this.owned_games = owned_games;
    }

    public ArrayList<String> getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(ArrayList<String> watchlist) {
        this.watchlist = watchlist;
    }
}
