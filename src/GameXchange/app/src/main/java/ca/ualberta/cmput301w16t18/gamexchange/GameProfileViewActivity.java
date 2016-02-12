package ca.ualberta.cmput301w16t18.gamexchange;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class GameProfileViewActivity extends ActionBarActivity {

    Game test = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_view);
    }

    public void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01
        //pull game information from server and display it
    }

}
