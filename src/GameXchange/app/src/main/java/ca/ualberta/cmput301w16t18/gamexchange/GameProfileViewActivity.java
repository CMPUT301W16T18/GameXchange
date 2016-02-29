package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

public class GameProfileViewActivity extends AppCompatActivity {

    public Game game;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_view);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra("id");
        loadGame(id);
    }

    public void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01

        // TODO: make ES query to fetch data about object
        game = new Game();

        // TODO: populate fields with received data
    }

}
