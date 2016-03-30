package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class UserProfileViewActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);
        Intent parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.USER_ID);
        loadUser(id);



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



    }

    // onClick method for the edit User button.
    @SuppressWarnings({"unused", "UnusedParameters"})
    public void editUserProfile(View view) {
        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }

}
