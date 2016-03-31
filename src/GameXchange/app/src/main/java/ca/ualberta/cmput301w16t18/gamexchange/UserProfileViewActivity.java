package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class UserProfileViewActivity extends AppCompatActivity {

    private String id;
    private View mProgressView;
    private View mListViewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);
        Intent parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.USER_ID);
        loadUser(id);

        mProgressView = findViewById(R.id.user_view_progress);
        mListViewView = findViewById(R.id.user_profile_ListView);
        showProgress(true);

        // This is for the Tutorial
        // For reuse statement https://github.com/deano2390/MaterialShowcaseView
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "UserView");

        sequence.setConfig(config);

        sequence.addSequenceItem(findViewById(R.id.tutorialTextViewUserView),
                "This is where you will see information about users!", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.tutorialTextViewUserView),
                "Contains basic information and reviews!", "GOT IT");

        sequence.start();
        TextView view = (TextView) findViewById(R.id.tutorialTextViewUserView);
        view.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser(id);
    }

    //Async loader for user information from elastic search
    private void loadUser(String userId) {
        //implements US 03.01.01
        ElasticSearcher.receiveUser(userId, this);
    }

    // Callback method for elastic search to populate the view.
    public void populateFields(User user) {

        //TODO: Remove this.
        ArrayList<Review> reviews = new ArrayList<>();
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        reviews.add(new Review(date, "", "", 5, "Best Game Ever!"));
        reviews.add(new Review(date, "", "", 4, "Great Game!"));
        reviews.add(new Review(date, "", "", 3, "Alright Game."));
        reviews.add(new Review(date, "", "", 2, "Not so good game."));
        reviews.add(new Review(date, "", "", 1, "Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n"));
        reviews.add(new Review(date, "", "", 0, "Would not recommend."));
        reviews.add(new Review(date, "", "", 1, "Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n Worst Game Ever! :( \n"));
        reviews.add(new Review(date, "", "", 2, "Not so good game."));
        reviews.add(new Review(date, "", "", 3, "Alright Game."));
        reviews.add(new Review(date, "", "", 4, "Great Game!"));
        reviews.add(new Review(date, "", "", 5, "Best Game Ever!"));
        user.setReviews(reviews);

        ListView listView = (ListView) findViewById(R.id.user_profile_ListView);
        ReviewListViewArrayAdapter adapter = new ReviewListViewArrayAdapter(this, user);
        adapter.setActivity(this);
        listView.setAdapter(adapter);

        showProgress(false);

    }

    // onClick method for the edit User button.
    @SuppressWarnings({"unused", "UnusedParameters"})
    public void editUserProfile(View view) {
        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(Constants.USER_ID, id);
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

            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListViewView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
