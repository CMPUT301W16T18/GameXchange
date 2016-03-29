package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by cawthorn on 2/28/16.
 */
public class ReviewListViewArrayAdapter extends ArrayAdapter<Review> {
    private final Context context;
    private final ArrayList<Review> reviews;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param reviews arrayList to be adaptered.
     */
    public ReviewListViewArrayAdapter(Context context, ArrayList<Review> reviews) {
        super(context,-1,reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchlist_listview_item, parent, false);
        }

        //Populate data in the view
        TextView textview = (TextView) convertView.findViewById(R.id.ReviewDateTextView);
        textview.setText(reviews.get(position).getDate().toString());

        textview = (TextView) convertView.findViewById(R.id.ReviewBodyTextView);
        textview.setText(reviews.get(position).getReviewBody());

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ReviewRatingBar);
        ratingBar.setNumStars(reviews.get(position).getRating());

        return convertView;
    }
}
