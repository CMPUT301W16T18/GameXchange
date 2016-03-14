package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
        ElasticSearcher.receiveGame(id, this, this.getLocalClassName());
    }

    public void populateFields(Game game) {
        EditText game_edit_title = (EditText) findViewById(R.id.game_edit_title);
        EditText game_edit_developer = (EditText) findViewById(R.id.game_edit_developer);
        EditText game_edit_platform = (EditText) findViewById(R.id.game_edit_platform);
        EditText game_edit_genres = (EditText) findViewById(R.id.game_edit_genres);
        EditText game_edit_description = (EditText) findViewById(R.id.game_edit_description);
        ImageView game_edit_image = (ImageView) findViewById(R.id.game_edit_image);

        game_edit_title.setText(game.getTitle());
        game_edit_developer.setText(game.getDeveloper());
        game_edit_platform.setText(game.getPlatform());
        game_edit_genres.setText(TextUtils.join(", ", game.getGenres()));
        game_edit_description.setText(game.getDescription());

        if (!game.getPicture().equals("")) {
            //Decode base64 string to a bitmap.
            byte[] decodedBytes = Base64.decode(game.getPicture(), 0);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            game_edit_image.setImageBitmap(imageBitmap);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void editGame() {
        //implements US 01.04.01

        // Get connection status
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) { cacheGame(); }

        EditText game_edit_title = (EditText) findViewById(R.id.game_edit_title);
        EditText game_edit_developer = (EditText) findViewById(R.id.game_edit_developer);
        EditText game_edit_platform = (EditText) findViewById(R.id.game_edit_platform);
        EditText game_edit_genres = (EditText) findViewById(R.id.game_edit_genres);
        EditText game_edit_description = (EditText) findViewById(R.id.game_edit_description);

        game.setTitle(game_edit_title.getText().toString());
        game.setDeveloper(game_edit_developer.getText().toString());
        game.setPlatform(game_edit_platform.getText().toString());
        game.setGenres(new ArrayList<String>( Arrays.asList(game_edit_genres.getText().toString().split("\\s*,\\s*"))));
        game.setDescription(game_edit_description.getText().toString());

        ElasticSearcher.sendGame(game, this);
        finish();
    }

    public void cacheGame() {
        //implements US 08.01.01
        // TODO: Actually cache the changed game info, along with timestamp
        finish();
    }

    public void takePhoto(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView image = (ImageView) findViewById(R.id.game_edit_image);
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            float aspectRatio = imageBitmap.getWidth() / (float) imageBitmap.getHeight();
            Bitmap smallBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (aspectRatio*200), 200, false);
            image.setImageBitmap(smallBitmap);

            //Encode bitmap to a base64 string.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(65536);
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
            String encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);

            ElasticSearcher.updateGamePicture(game.getId(), encoded, this);
        }
    }
}
