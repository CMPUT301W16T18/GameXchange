package ca.ualberta.cmput301w16t18.gamexchange;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class UserProfileViewActivity extends ActionBarActivity {

    public User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);
    }

    public void loadUser(String userId) {
        //implements US 03.01.01
        //pull user information from server and display it

        //Hardcoded values here
        me = new User();

        if (userId.equals("USER-ID")) {
            me.setName("User's Name");
        }
    }

}
