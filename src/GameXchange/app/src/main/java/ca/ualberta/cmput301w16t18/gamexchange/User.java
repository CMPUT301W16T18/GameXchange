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
    private ArrayList<String> watchlist;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return id;
    }
}
