package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

public class UserProfileEditActivity extends AppCompatActivity {

    public User user;
    private Intent parent_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        parent_intent = getIntent();
        String id = parent_intent.getStringExtra("id");

        // TODO: make ES query to fetch data about object
        user = new User();

        // TODO: populate fields with received data
    }

    public void editUser(User myuser) {
        //implements US 03.02.01
        //edit all fields for a user and save the changes
        myuser.setName("Bill");
    }

}
