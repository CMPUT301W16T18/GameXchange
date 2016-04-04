package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is the adapter for bids in the listview
 */
class BidListViewArrayAdapter extends ArrayAdapter<Bid> {

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

        try {
            String text = String.format("$%.2f / day", bid.getPrice());
            textview.setText(text);
        } catch (NullPointerException ex) {
            System.out.println("text was null.");
            textview.setText("Text was null");
        }
        return convertView;
    }
}