package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

public class UserProfileViewActivity extends AppCompatActivity {

    public User user;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra("id");
        loadUser(id);
    }

    public void loadUser(String userId) {
        //implements US 03.01.01

        // TODO: make ES query to fetch data about object
        user = new User();

        // TODO: populate fields with received data
    }

}
