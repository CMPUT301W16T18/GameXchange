package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class GameProfileEditActivity extends AppCompatActivity {

    public Game game;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_edit);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);
        loadGame(id);
        Button cancel = (Button) findViewById(R.id.game_edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) { finish(); }
          });

        Button save = (Button) findViewById(R.id.game_edit_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { editGame(); }
        });
    }

    public void loadGame(String id) {
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

        // TODO: send data back to ES server. Refer by id.
        finish();
    }

    public void cacheGame() {
        //implements US 08.01.01
        // TODO: Actually cache the changed game info, along with timestamp
    }

    public void takePhoto(View view) {
        Toast.makeText(this, "you don't need a photo, its a game...",Toast.LENGTH_SHORT).show();
    }

}
