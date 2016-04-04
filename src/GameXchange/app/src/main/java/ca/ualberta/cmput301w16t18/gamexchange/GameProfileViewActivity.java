package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GameProfileViewActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int PLACE_PICKER_REQUEST = 2;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private String id;
    private View mProgressView;
    private View mView;
    private ListView listView;
    private View headerView;
    private BidListViewArrayAdapter adapter;
    private Game game;
    private Review reviewToPostOnElasticSearchCallback;

    private Place place;

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
        LayoutInflater inflater = this.getLayoutInflater();
        headerView = inflater.inflate(R.layout.game_profile_listview_item, listView, false);
        listView.addHeaderView(headerView);

        if (Constants.CURRENT_USER.getGames().contains(id)){
            registerForContextMenu(listView);
        }


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

    // Adapted from http://developer.android.com/training/basics/intents/result.html#ReceiveResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PLACE_PICKER_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a place.
                // The Intent's data holds the place.
                Place place = PlacePicker.getPlace(this, data);
                this.place = place;
                Toast.makeText(this, "Place: " + place.getName(), Toast.LENGTH_SHORT).show();
                // Do something with the contact here (bigger example below)
                showBidDialog();
            }
        }
    }

    private void loadGame(String id) {
        //implements US 01.03.01 and US 03.03.01
        ElasticSearcher.receiveGame(id, this);
    }

    public void populateFields(Game game) {

        this.game = game;
        adapter = new BidListViewArrayAdapter(this, game.getBids());
        updateHeader();
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
        //TODO: This. should be done, needs to be tested.
        bid.setStatus(Constants.ACCEPTED);
        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(bid);
        game.setBids(bids);
        game.setStatus(Constants.BORROWED);
        ElasticSearcher.sendGame(game);
        String borrowingUser = bid.getBidder();
        ElasticSearcher.receiveUser(borrowingUser,this);
    }

    //TODO: This should be done but needs to be tested when bids sync to elastic search.
    public void elasticSearcherCallback(User user) {
        ArrayList<String> borrowing = user.getBorrowing();
        borrowing.add(game.getId());
    }

    private void viewBidLocation(Bid bid) {
        //From https://developers.google.com/maps/documentation/android-api/intents#display_a_map
        Uri gmmIntentUri = Uri.parse(bid.getMapsString());
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void viewBidBidder(Bid bid) {
        //TODO: This. Should be done, needs to be tested.
        Intent intent = new Intent(GameProfileViewActivity.this, UserProfileViewActivity.class);
        intent.putExtra(Constants.USER_ID, bid.getBidder());
        startActivity(intent);
    }

    private void declineBid(Bid bid) {
        //TODO: This. Removing a bid does not sync with elastic search.
        ArrayList<Bid> bids = game.getBids();
        bids.remove(bid);
        game.setBids(bids);
        adapter.remove(bid);
        adapter.notifyDataSetChanged();
        ElasticSearcher.sendGame(game);
    }

    //adapted from https://bhavyanshu.me/tutorials/create-custom-alert-dialog-in-android/08/20/2015
    public void showBidDialog() {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.bid_dialog_layout,null);

        builder.setTitle("Bid Amount")
                .setView(view)
                .setPositiveButton("Place bid", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        final EditText editText = (EditText) view.findViewById(R.id.bid_dialog_edittext);
                        Bid bid = new Bid(Constants.CURRENT_USER.getId(), Double.valueOf(editText.getText().toString()), place.getLatLng().latitude, place.getLatLng().longitude);
                        updateBidList(bid);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing, user canceled.

                    }
                });
        builder.create().show();
    }

    //adapted from https://bhavyanshu.me/tutorials/create-custom-alert-dialog-in-android/08/20/2015
    public void showReviewDialog() {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View reviewView = inflater.inflate(R.layout.review_dialog_layout, null);
        final EditText editText = (EditText) reviewView.findViewById(R.id.ReviewDialogEditText);
        final RatingBar rating = (RatingBar) reviewView.findViewById(R.id.ReviewDialogRatingBar);

        builder.setTitle("Review Borrower")
                .setView(reviewView)
                .setPositiveButton("Save Review", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        Review review = new Review(System.currentTimeMillis(),
                                editText.getText().toString(), rating.getRating(),
                                Constants.CURRENT_USER.getId() , game.getId());
                        game.setStatus(Constants.AVAILABLE);
                        //ElasticSearcher.sendGame(game); TODO: Uncomment.
                        //TODO: attach this to a user, as well as removing the game from there list of borrowed games.
                        reviewToPostOnElasticSearchCallback = review;
                        ElasticSearcher.getBorrowingUser(GameProfileViewActivity.this, game.getId());
                    }
                })
                .setNeutralButton("Don't Review", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set game available, but don't save review.
                        game.setStatus(Constants.AVAILABLE);
                        ElasticSearcher.sendGame(game);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing, user canceled.

                    }
                });
        builder.create().show();
    }

    private void updateHeader() {

        TextView game_view_title = (TextView) headerView.findViewById(R.id.game_view_title);
        TextView game_view_developer = (TextView) headerView.findViewById(R.id.game_view_developer);
        TextView game_view_platform = (TextView) headerView.findViewById(R.id.game_view_platform);
        TextView game_view_genres = (TextView) headerView.findViewById(R.id.game_view_genres);
        TextView game_view_description = (TextView) headerView.findViewById(R.id.game_view_description);
        ImageView game_view_image = (ImageView) headerView.findViewById(R.id.game_view_image);

        game_view_title.setText(game.getTitle());
        game_view_developer.setText(game.getDeveloper());
        game_view_platform.setText(game.getPlatform());
        game_view_genres.setText(TextUtils.join(", ", game.getGenres()));
        game_view_description.setText(game.getDescription());

        if (Constants.CURRENT_USER.getGames().contains(game.getId())){

            View view = headerView.findViewById(R.id.game_edit_button);
            View view1 = headerView.findViewById(R.id.game_edit_return);
            view.setVisibility(view.VISIBLE);

            if (game.getStatus().equals(Constants.BORROWED)) {
                view1.setVisibility(view1.VISIBLE);
                view1.setOnClickListener(returnListener);
            }
        }
        else{

            View view = headerView.findViewById(R.id.game_edit_bid);
            View view1 = headerView.findViewById(R.id.game_edit_watchlist);
            view.setVisibility(view.VISIBLE);
            view1.setVisibility(view1.VISIBLE);
            view.setOnClickListener(bidListener);
            view1.setOnClickListener(watchListener);
        }


        if (!game.getPicture().equals("")) {
            //Decode base64 string to a bitmap.
            byte[] decodedBytes = Base64.decode(game.getPicture(), 0);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            game_view_image.setImageBitmap(imageBitmap);
        }
    }

    // TODO : Vassili needs to remove games from other lists
    public View.OnClickListener returnListener = new View.OnClickListener() {
        public void onClick(View v) {
            showReviewDialog();
        }
    };

    public View.OnClickListener bidListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(!mayRequestLocation()) {
                //Do nothing, user cannot make a bid if the permission is denied.
            } else {
                makeBid();
            }
        }
    };

    public View.OnClickListener watchListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (Constants.CURRENT_USER.getWatchlist().contains(game.getId())){
                Toast.makeText(GameProfileViewActivity.this, game.getTitle() + " Already Watchlisted", Toast.LENGTH_LONG).show();
            }else{
                ArrayList<String> watch = Constants.CURRENT_USER.getWatchlist();
                watch.add(game.getId());
                Constants.CURRENT_USER.setWatchlist(watch);
                ElasticSearcher.sendUser(Constants.CURRENT_USER);
                Toast.makeText(GameProfileViewActivity.this, game.getTitle() + " was added to your watchlist.", Toast.LENGTH_LONG).show();
            }

        }
    };

    // Modified from http://stackandroid.com/tutorial/android-autocomplete-email-ids-using-loadermanager-on-marshmallow/
    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
            Snackbar.make(findViewById(R.id.game_profile_ListView), R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(getParent(), new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
        return false;
    }

    // called by ActivityCompat.requestPermissions, from android documentation
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted.
                    makeBid();
                } else {
                    // permission was denied. do nothing.
                    Snackbar.make(findViewById(R.id.game_profile_ListView),"Location access is needed to make bids.",
                            Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Snackbar is dismissed on click by design, so do nothing.
                        }
                    });
                }

            }
        }
    }

    private void makeBid() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A new window will open to select the location the trade will take place.")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, want the dialog to close.
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPlacePicker();
                    }
                });
        builder.show();
    }

    private void startPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException ex) {
            Snackbar.make(findViewById(R.id.game_profile_ListView),"Google Play Services is required for this application.",Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Do nothing, want the snackbar to be dismissed.
                        }
                    }).show();
        } catch (GooglePlayServicesRepairableException ex) {
            Snackbar.make(findViewById(R.id.game_profile_ListView),"Google Play Services needs to be up to date and enabled for this application.",Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Do nothing, want the snackbar to be dismissed.
                        }
                    }).show();
        }
    }

    private void updateBidList(Bid bid) {
        ArrayList<Bid> bids = game.getBids();
        bids.add(bid);
        game.setBids(bids);
        adapter = new BidListViewArrayAdapter(this, game.getBids());
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        ElasticSearcher.sendGame(game);
    }

    public void saveReviewAndRemoveBorrowedGame(User user) {
        ArrayList<String> borrowing = user.getBorrowing();
        borrowing.remove(game.getId());
        user.setBorrowing(borrowing);

        ArrayList<Review> reviews = user.getReviews();
        reviews.add(reviewToPostOnElasticSearchCallback);
        user.setReviews(reviews);

        //ElasticSearcher.sendUser(user); TODO: Uncomment.
    }
}
