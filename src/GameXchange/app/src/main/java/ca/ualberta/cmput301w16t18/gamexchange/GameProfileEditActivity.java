package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class GameProfileEditActivity extends AppCompatActivity {

    private Game game;

    private View mProgressView;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_edit);

        mProgressView = findViewById(R.id.game_edit_view_progress);
        mView = findViewById(R.id.game_profile_edit_all_view);

        Intent parent_intent = getIntent(); //fixed as per lint.
        String id = parent_intent.getStringExtra(Constants.GAME_ID);
        if (id.equals("")) {
            game = new Game();

            setTitle("Let's add a game to your list!");
        }
        else {
            loadGame(id);
        }

        Button save = (Button) findViewById(R.id.game_edit_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGame();
            }
        });
    }

    public void loadGame(String id) {
            showProgress(true);                
            ElasticSearcher.receiveGame(id, this);
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

        showProgress(false);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    protected void editGame() {
        //implements US 01.04.01

        // Get connection status
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        EditText game_edit_title = (EditText) findViewById(R.id.game_edit_title);
        EditText game_edit_developer = (EditText) findViewById(R.id.game_edit_developer);
        EditText game_edit_platform = (EditText) findViewById(R.id.game_edit_platform);
        EditText game_edit_genres = (EditText) findViewById(R.id.game_edit_genres);
        EditText game_edit_description = (EditText) findViewById(R.id.game_edit_description);

        game.setTitle(game_edit_title.getText().toString());
        game.setDeveloper(game_edit_developer.getText().toString());
        game.setPlatform(game_edit_platform.getText().toString());
        game.setGenres(new ArrayList<>(Arrays.asList(game_edit_genres.getText().toString().split("\\s*,\\s*"))));
        game.setDescription(game_edit_description.getText().toString());

        if (!isConnected) {
            cacheGame(game);
            CharSequence text = "Cached Till Network Available";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
        else {
            if (game.getId().equals("")) {
                game.setStatus("Available");
            }
            ElasticSearcher.sendGame(game);
            CharSequence text = "Saved!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }


    }

    private void cacheGame(Game game) {
        //implements US 08.01.01
        saveInFile(game);
    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void takePhoto(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        }
    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void deletePhoto(View view) {
        ImageView image = (ImageView) findViewById(R.id.game_edit_image);
        image.setImageBitmap(null);
        game.setPicture("");
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
            game.setPicture(encoded);

            //ElasticSearcher.updateGamePicture(game.getId(), encoded, this);
        }
    }


    /**
     * This method used for Caching
     */
    public void saveInFile(Game game) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),"")+ File.separator+Constants.FILENAME+Constants.iterator));
            Constants.iterator = Constants.iterator +1;
            oos.writeObject(game);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
