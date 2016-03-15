package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                .singleUse("Show edit") // provide a unique ID used to ensure it is only shown once
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parent_intent = getIntent();
        id = parent_intent.getStringExtra(Constants.USER_ID);
        loadUser(id);
    }

    //Async loader for user information from elastic search
    public void loadUser(String userId) {
        //implements US 03.01.01
        ElasticSearcher.receiveUser(Constants.CURRENT_USER, this, "UserProfileViewActivity");
    }

    // Callback method for elastic search to populate the view.
    public void populateFields(User user) {
        TextView viewUserName = (TextView) findViewById(R.id.viewUserName);
        TextView viewUserEmail = (TextView) findViewById(R.id.viewUserEmail);
        TextView viewUserAddress1 = (TextView) findViewById(R.id.viewUserAddress1);
        TextView viewUserAddress2 = (TextView) findViewById(R.id.viewUserAddress2);
        TextView viewUserCity = (TextView) findViewById(R.id.viewUserCity);
        TextView viewUserPhone = (TextView) findViewById(R.id.viewUserPhone);
        TextView viewUserPostalCode = (TextView) findViewById(R.id.viewUserPostalCode);

        viewUserName.setText(user.getName());
        viewUserEmail.setText(user.getEmail());
        viewUserAddress1.setText(user.getAddress1());
        viewUserAddress2.setText(user.getAddress2());
        viewUserCity.setText(user.getCity());
        viewUserPhone.setText(user.getPhone());
        viewUserPostalCode.setText(user.getPostal());
    }

    // onClick method for the edit User button.
    public void editUserProfile(View view) {
        Intent intent = new Intent(this, UserProfileEditActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }

}
