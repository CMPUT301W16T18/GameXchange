package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GameProfileViewActivity extends AppCompatActivity {

    public Game game;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_view);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);
        loadGame(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);

    }

    public void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01

        // TODO: make ES query to fetch data about object
        game = new Game();

        // TODO: populate fields with received data
    }

    public void editGameProfile(View view) {
        Intent intent = new Intent(this,GameProfileEditActivity.class);
        intent.putExtra(Constants.GAME_ID, id);
        startActivity(intent);
    }

}
