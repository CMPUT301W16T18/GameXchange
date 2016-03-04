package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class UserProfileViewActivity extends AppCompatActivity {

    public User user;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.USER_ID);
        loadUser(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.USER_ID);
        loadUser(id);
    }

    public void loadUser(String userId) {
        //implements US 03.01.01
        ElasticSearcher.receiveUser("AVM1KtaDI8oCfzIHasfN", this, "UserProfileViewActivity");
    }

    public void editUserProfile(View view) {
        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }

}
