package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class GameProfileViewActivity extends AppCompatActivity {

    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile_view);
        Intent parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.GAME_ID);
        loadGame(id);
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
        switch(item.getItemId()) {
            case R.id.bid_accept:
                //  stuff here
                return true;
            case R.id.bid_view_location:
                //  stuff here
                return true;
            case R.id.bid_view_bidder:
                //  stuff here
                return true;
            case R.id.bid_decline:
                //  stuff here
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
        bids.add(new Bid(Constants.CURRENT_USER, 19.99, new LatLng(53.33, 113.33)));
        bids.add(new Bid(Constants.CURRENT_USER, 20.99, new LatLng(53.33, 113.33)));
        bids.add(new Bid(Constants.CURRENT_USER, 9.99, new LatLng(53.33, 113.33)));
        bids.add(new Bid(Constants.CURRENT_USER, 199.99, new LatLng(53.33, 113.33)));
        game.setBids(bids);

        ListView listView = (ListView) findViewById(R.id.game_profile_ListView);
        BidListViewArrayAdapter adapter = new BidListViewArrayAdapter(this, game);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        /*
        new MaterialShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.game_edit_button))
                .setDismissText("GOT IT")
                .setContentText("Touch here to edit the info about your game and add an image !!")
                .setDelay(1) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Show edit game") // provide a unique ID used to ensure it is only shown once
                .show();
        */
    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void editGameProfile(View view) {
        Intent intent = new Intent(this, GameProfileEditActivity.class);
        intent.putExtra(Constants.GAME_ID, id);
        startActivity(intent);
    }

    private void setupListView(ArrayList<Bid> bids) {

    }

}
