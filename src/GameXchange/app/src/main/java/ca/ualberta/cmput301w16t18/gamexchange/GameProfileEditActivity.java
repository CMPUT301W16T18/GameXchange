package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class GameProfileEditActivity extends ActionBarActivity {

    public Game game;
    private Intent parent_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_edit);
        parent_intent = getIntent();
        String id = parent_intent.getStringExtra("id");

        // TODO: make ES query to fetch data about object
        game = new Game();

        // TODO: populate fields with received data
    }

    public void editGame() {
        //implements US 01.04.01

        // Get connection status
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        

        if (!isConnected) { cacheGame(); }

        // TODO: send data back to ES server
        finish();
    }

    public void cacheGame() {
        //implements US 08.01.01
        // TODO: Actually cache the changed game info, along with timestamp
    }

}
