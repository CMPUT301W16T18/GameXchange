package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

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

        Button editButton = (Button) findViewById(R.id.viewUserEdit);
        new MaterialShowcaseView.Builder(this)
                .setTarget(editButton)
                .setDismissText("GOT IT")
                .setContentText("Click here to edit a user!")
                .setDelay(1) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("Show once") // provide a unique ID used to ensure it is only shown once
                .show();
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
        ElasticSearcher.receiveUser(Constants.CURRENT_USER, this, "UserProfileViewActivity");
    }

    public void editUserProfile(View view) {
        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }

}
