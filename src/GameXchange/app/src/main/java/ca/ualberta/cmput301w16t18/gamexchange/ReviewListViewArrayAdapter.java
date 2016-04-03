package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by cawthorn on 2/28/16.
 */
public class ReviewListViewArrayAdapter extends ArrayAdapter<Review> {
    private final Context context;
    private final ArrayList<Review> reviews;
    private final User user;
    private Activity activity;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param user User to be adaptered.
     */
    public ReviewListViewArrayAdapter(Context context, User user) {
        super(context,-1,user.getReviews());
        this.context = context;
        this.user = user;
        this.reviews = user.getReviews();
        this.add(new Review());
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
            convertView = inflater.inflate(R.layout.user_profile_listview_item, parent, false);

            TextView viewUserName = (TextView) convertView.findViewById(R.id.viewUserName);
            TextView viewUserEmail = (TextView) convertView.findViewById(R.id.viewUserEmail);
            TextView viewUserAddress1 = (TextView) convertView.findViewById(R.id.viewUserAddress1);
            TextView viewUserAddress2 = (TextView) convertView.findViewById(R.id.viewUserAddress2);
            TextView viewUserCity = (TextView) convertView.findViewById(R.id.viewUserCity);
            TextView viewUserPhone = (TextView) convertView.findViewById(R.id.viewUserPhone);
            TextView viewUserPostalCode = (TextView) convertView.findViewById(R.id.viewUserPostalCode);

            viewUserName.setText(user.getName());
            viewUserEmail.setText(user.getEmail());
            viewUserAddress1.setText(user.getAddress1());
            viewUserAddress2.setText(user.getAddress2());
            viewUserCity.setText(user.getCity());
            viewUserPhone.setText(user.getPhone());
            viewUserPostalCode.setText(user.getPostal());

            Button editButton = (Button) convertView.findViewById(R.id.viewUserEdit);
            new MaterialShowcaseView.Builder(activity)
                .setTarget(editButton)
                .setDismissText("GOT IT")
                .setContentText("Click here to edit a user!")
                .setDelay(1) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Show edit") // provide a unique ID used to ensure it is only shown once
                .show();
        } else {
            convertView = inflater.inflate(R.layout.review_listview_item, parent, false);

            //Populate data in the view
            Review review = reviews.get(position - 1);
            TextView textview = (TextView) convertView.findViewById(R.id.ReviewDateTextView);
            textview.setText(review.getDateString());

            textview = (TextView) convertView.findViewById(R.id.ReviewBodyTextView);
            textview.setText(review.getReviewBody());

            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ReviewRatingBar);
            ratingBar.setNumStars(5);
            ratingBar.setRating(review.getRating());
        }
        return convertView;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
