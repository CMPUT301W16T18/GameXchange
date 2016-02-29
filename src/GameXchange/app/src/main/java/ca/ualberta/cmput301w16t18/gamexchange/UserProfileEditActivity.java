package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class UserProfileEditActivity extends AppCompatActivity {

    public User user;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra("id");
        loadUser();
        Button cancel = (Button) findViewById(R.id.game_edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });

        Button save = (Button) findViewById(R.id.game_edit_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { editUser(); }
        });
    }

    public void loadUser() {
        // TODO: make ES query to fetch data about object
        user = new User();

        // TODO: populate fields with received data
    }

    public void editUser() {
        //implements US 03.02.01

        // TODO: send data back to ES server. Refer by id.
        finish();
    }

}
