package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserProfileEditActivity extends AppCompatActivity {

    private User user;
    private Intent parent_intent;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);
        parent_intent = getIntent();
        id = parent_intent.getStringExtra("id");
        loadUser();
    }

    /**
     * returns the current user being displayed
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * call to elastic search to load the user into the view.
     */
    public void loadUser() {
        ElasticSearcher.receiveUser(Constants.CURRENT_USER, this, "UserProfileEditActivity");
    }

    /**
     * callback from elastic search to populate the view with the user.
     * @param user
     */
    public void populateFields(User user) {
        EditText editUserName = (EditText) findViewById(R.id.editUserName);
        EditText editUserEmail = (EditText) findViewById(R.id.editUserEmail);
        EditText editUserAddress1 = (EditText) findViewById(R.id.editUserAddress1);
        EditText editUserAddress2 = (EditText) findViewById(R.id.editUserAddress2);
        EditText editUserCity = (EditText) findViewById(R.id.editUserCity);
        EditText editUserPhone = (EditText) findViewById(R.id.editUserPhone);
        EditText editUserPostalCode = (EditText) findViewById(R.id.editUserPostalCode);

        editUserName.setText(user.getName());
        editUserEmail.setText(user.getEmail());
        editUserAddress1.setText(user.getAddress1());
        editUserAddress2.setText(user.getAddress2());
        editUserCity.setText(user.getCity());
        editUserPhone.setText(user.getPhone());
        editUserPostalCode.setText(user.getPostal());
    }

    /**
     * sets the user for the current view
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * saves the edited user to elastic search.
     * @param view
     */
    public void editUser(View view) {
        //implements US 03.02.01
        EditText editUserName = (EditText) findViewById(R.id.editUserName);
        EditText editUserEmail = (EditText) findViewById(R.id.editUserEmail);
        EditText editUserAddress1 = (EditText) findViewById(R.id.editUserAddress1);
        EditText editUserAddress2 = (EditText) findViewById(R.id.editUserAddress2);
        EditText editUserCity = (EditText) findViewById(R.id.editUserCity);
        EditText editUserPhone = (EditText) findViewById(R.id.editUserPhone);
        EditText editUserPostalCode = (EditText) findViewById(R.id.editUserPostalCode);
        EditText editUserPassword1 = (EditText) findViewById(R.id.editUserPassword1);
        EditText editUserPassword2 = (EditText) findViewById(R.id.editUserPassword2);
        EditText editUserPassword3 = (EditText) findViewById(R.id.editUserPassword3);

        user.setName(editUserName.getText().toString());
        user.setEmail(editUserEmail.getText().toString());
        user.setAddress1(editUserAddress1.getText().toString());
        user.setAddress2(editUserAddress2.getText().toString());
        user.setCity(editUserCity.getText().toString());
        user.setPhone(editUserPhone.getText().toString());
        user.setPostal(editUserPostalCode.getText().toString());

        String pass1 = editUserPassword1.getText().toString();
        String pass2 = editUserPassword2.getText().toString();
        String pass3 = editUserPassword3.getText().toString();

        if (pass1.equals("") && pass2.equals("") && pass3.equals("")) {
            ElasticSearcher.sendUser(user, this);
            CharSequence text = "Saved!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }

        if (! Hasher.getHash(pass1).equals(user.getPasshash())) {
            editUserPassword1.setError("The entered password is incorrect.");
            return;
        }

        if (! Constants.isPasswordValid(pass2)) {
            editUserPassword2.setError("The chosen password is invalid.");
            return;
        }

        if (! pass2.equals(pass3)) {
            editUserPassword3.setError("The new passwords do not match.");
            return;
        }

        user.setPasshash(Hasher.getHash(pass2));
        ElasticSearcher.sendUser(user, this);
        CharSequence text = "Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
        editUserPassword1.setText("");
        editUserPassword2.setText("");
        editUserPassword3.setText("");

    }
}
