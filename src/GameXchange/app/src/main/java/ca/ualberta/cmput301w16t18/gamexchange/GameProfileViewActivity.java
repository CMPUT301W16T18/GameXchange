package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class GameProfileViewActivity extends AppCompatActivity {

    private String id;

    private View mProgressView;
    private View mView;

    private ListView listView;
    private BidListViewArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_view);
        Intent parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);
        loadGame(id);

        mView = findViewById(R.id.game_profile_ListView);
        mProgressView = findViewById(R.id.game_view_progress);

        showProgress(true);

        listView = (ListView) findViewById(R.id.game_profile_ListView);
        adapter = new BidListViewArrayAdapter(this, new Game("", "", "", "", "",
                new ArrayList<String>(), "", "", new ArrayList<Bid>()));
        listView.setAdapter(adapter);

        registerForContextMenu(listView);


        // For reuse statement https://github.com/deano2390/MaterialShowcaseView
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "GameView");

        sequence.setConfig(config);

        sequence.addSequenceItem(findViewById(R.id.tutorialTextViewGameView),
                "This is where you will see the information for a single game!", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.tutorialTextViewGameView),
                "It contains game information and bid information!", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.tutorialTextViewGameView),
                "To view location of a bid, decline or accept a bid, and to view the location of" +
                        " a bid long press on a bid!", "GOT IT");

        sequence.start();
        TextView view = (TextView) findViewById(R.id.tutorialTextViewGameView);
        view.setVisibility(View.GONE);



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.game_profile_ListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.bid_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ListView listView = (ListView) findViewById(R.id.game_profile_ListView);
        Bid bid = (Bid) listView.getItemAtPosition(info.position);
        switch(item.getItemId()) {
            case R.id.bid_accept:
                acceptBid(bid);
                return true;
            case R.id.bid_view_location:
                viewBidLocation(bid);
                return true;
            case R.id.bid_view_bidder:
                viewBidBidder(bid);
                return true;
            case R.id.bid_decline:
                declineBid(bid);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGame(id);
    }

    private void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01
        ElasticSearcher.receiveGame(id, this);
    }

    public void populateFields(Game game) {


        //TODO: Remove this.
        ArrayList<Bid> bids = new ArrayList<Bid>();
        bids.add(new Bid(Constants.CURRENT_USER.getId(), 19.99, new LatLng(53.55, -113.5)));
        bids.add(new Bid(Constants.CURRENT_USER.getId(), 20.99, new LatLng(53.55, -113.5)));
        bids.add(new Bid(Constants.CURRENT_USER.getId(), 9.99, new LatLng(53.55, -113.5)));
        bids.add(new Bid(Constants.CURRENT_USER.getId(), 199.99, new LatLng(53.55, -113.5)));
        game.setBids(bids);

        adapter = new BidListViewArrayAdapter(this, game);
        listView.setAdapter(adapter);

        showProgress(false);


    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void editGameProfile(View view) {
        Intent intent = new Intent(this, GameProfileEditActivity.class);
        intent.putExtra(Constants.GAME_ID, id);
        startActivity(intent);
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

    private void acceptBid(Bid bid) {
        //TODO: This. need to clear all bids on the object and mark one as accepted. 

    }

    private void viewBidLocation(Bid bid) {
        //From https://developers.google.com/maps/documentation/android-api/intents#display_a_map
        Uri gmmIntentUri = Uri.parse(bid.getMapsString());
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void viewBidBidder(Bid bid) {
        //TODO: This.
        Intent intent = new Intent(GameProfileViewActivity.this, UserProfileViewActivity.class);
        intent.putExtra(Constants.USER_ID,bid.getBidder());
        startActivity(intent);
    }

    private void declineBid(Bid bid) {
        //TODO: This. need to remove the bid from the arraylist and resync bids with elastic search.

    }

}
