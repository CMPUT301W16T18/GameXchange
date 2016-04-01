package ca.ualberta.cmput301w16t18.gamexchange;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by cawthorn on 2/28/16.
 */
public class BidListViewArrayAdapter extends ArrayAdapter<Bid> implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final Context context;
    private ArrayList<Bid> bids;
    private Game game;
    private Activity activity;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param game game to be adaptered.
     */
    public BidListViewArrayAdapter(Activity activity, Context context, Game game) {
        super(context,-1,game.getBids());
        this.context = context;
        this.game = game;
        this.bids = game.getBids();
        this.activity = activity;
        this.add(new Bid());
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if(position == 0) return 0;
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getItemViewType(position) == 0) {
            convertView = inflater.inflate(R.layout.game_profile_listview_item, parent, false);
            TextView game_view_title = (TextView) convertView.findViewById(R.id.game_view_title);
            TextView game_view_developer = (TextView) convertView.findViewById(R.id.game_view_developer);
            TextView game_view_platform = (TextView) convertView.findViewById(R.id.game_view_platform);
            TextView game_view_genres = (TextView) convertView.findViewById(R.id.game_view_genres);
            TextView game_view_description = (TextView) convertView.findViewById(R.id.game_view_description);
            ImageView game_view_image = (ImageView) convertView.findViewById(R.id.game_view_image);

            game_view_title.setText(game.getTitle());
            game_view_developer.setText(game.getDeveloper());
            game_view_platform.setText(game.getPlatform());
            game_view_genres.setText(TextUtils.join(", ", game.getGenres()));
            game_view_description.setText(game.getDescription());

            if (Constants.CURRENT_USER.getGames().contains(game.getId())){
                View view = convertView.findViewById(R.id.game_edit_button);
                view.setVisibility(view.VISIBLE);
            }
            else{
                View view = convertView.findViewById(R.id.game_edit_bid);
                View view1 = convertView.findViewById(R.id.game_edit_watchlist);
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
        } else {

            convertView = inflater.inflate(R.layout.bid_listview_item, parent, false);

            //Populate data in the view
            Bid bid = bids.get(position - 1);

            TextView textview = (TextView) convertView.findViewById(R.id.BidListItemItemAmountTextView);
            String text = String.valueOf(bid.getPrice());
            try {
                textview.setText("$" + text + " / day");
            } catch (NullPointerException ex) {
                System.out.println("text was null.");
                textview.setText("Text was null");
            }
        }


        return convertView;
    }

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
            // DO something
        }
    };

    // Modified from http://stackandroid.com/tutorial/android-autocomplete-email-ids-using-loadermanager-on-marshmallow/
    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_FINE_LOCATION)) {
            Snackbar.make(activity.findViewById(R.id.game_profile_ListView),R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
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
                    Snackbar.make(activity.findViewById(R.id.game_profile_ListView),"Location access is needed to make bids.",
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
        Snackbar.make(activity.findViewById(R.id.game_profile_ListView), "Needs to be implemented.",
                Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar is dismissed on click by design, so do nothing.
            }
        }).show();
    }
}




