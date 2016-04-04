package ca.ualberta.cmput301w16t18.gamexchange;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by cawthorn on 2/28/16.
 */
public class BidListViewArrayAdapter extends ArrayAdapter<Bid> {

    private final Context context;
    private ArrayList<Bid> bids;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param bids bids to be adaptered.
     */
    public BidListViewArrayAdapter(Context context, ArrayList<Bid> bids) {
        super(context,-1,bids);
        this.context = context;
        this.bids = bids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bid_listview_item, parent, false);
        }

        //Populate data in the view
        Bid bid = bids.get(position);

        TextView textview = (TextView) convertView.findViewById(R.id.BidListItemItemAmountTextView);
        String text = String.valueOf(bid.getPrice());
        try {
            textview.setText("$" + text + " / day");
        } catch (NullPointerException ex) {
            System.out.println("text was null.");
            textview.setText("Text was null");
        }
        return convertView;
    }
}