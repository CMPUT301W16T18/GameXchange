package ca.ualberta.cmput301w16t18.gamexchange;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class UserProfileEditActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
    }

    public void editUser(User myuser) {
        //implements US 03.02.01
        //edit all fields for a user and save the changes
        myuser.setName("Bill");
    }

}
