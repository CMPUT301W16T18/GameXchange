package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

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

        new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.game_edit_button))
                .setDismissText("GOT IT")
                .setContentText("Touch here to edit the info about your game and add an image !!")
                .setDelay(1) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Show edit game") // provide a unique ID used to ensure it is only shown once
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);
    }

    public void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01
        ElasticSearcher.receiveGame(id, this, this.getLocalClassName());
    }

    public void populateFields(Game game) {
        TextView game_view_title = (TextView) findViewById(R.id.game_view_title);
        TextView game_view_developer = (TextView) findViewById(R.id.game_view_developer);
        TextView game_view_platform = (TextView) findViewById(R.id.game_view_platform);
        TextView game_view_genres = (TextView) findViewById(R.id.game_view_genres);
        TextView game_view_description = (TextView) findViewById(R.id.game_view_description);

        game_view_title.setText(game.getTitle());
        game_view_developer.setText(game.getDeveloper());
        game_view_platform.setText(game.getPlatform());
        game_view_genres.setText(TextUtils.join(", ", game.getGenres()));
        game_view_description.setText(game.getDescription());
    }

    public void editGameProfile(View view) {
        Intent intent = new Intent(this, GameProfileEditActivity.class);
        intent.putExtra(Constants.GAME_ID, id);
        startActivity(intent);
    }

}
