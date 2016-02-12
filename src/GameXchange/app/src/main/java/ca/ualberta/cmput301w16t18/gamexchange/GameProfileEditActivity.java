package ca.ualberta.cmput301w16t18.gamexchange;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class GameProfileEditActivity extends ActionBarActivity {

    public GameList games = new GameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_edit);
    }

    public void editGame(Game mygame) {
        //implements US 01.04.01
        //edit all fields for a game and save the changes
    }

    public void cacheGame(Game mygame) {
        //implements US 08.01.01
        //if no network connectivity is detected, save game info locally.
    }

}
