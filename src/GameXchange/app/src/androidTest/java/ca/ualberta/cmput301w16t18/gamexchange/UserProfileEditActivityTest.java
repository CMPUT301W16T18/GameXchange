package ca.ualberta.cmput301w16t18.gamexchange;

import android.widget.EditText;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class UserProfileEditActivityTest extends TestCase {

    public void testSetUser() {
        UserProfileEditActivity activity = new UserProfileEditActivity();
        User user = new User("TEST_ID","Email","Name","Passhash","Address1","Address2","City",
                "Phone","postal",new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),
                new ArrayList<Review>());
        activity.setUser(user);
        assertEquals("User is not equal",user,activity.getUser());
    }

    public void testPopulateFields() {
        UserProfileEditActivity activity = new UserProfileEditActivity();
        User user = new User("TEST_ID","Email","Name","Passhash","Address1","Address2","City",
                "Phone","postal",new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),
                new ArrayList<Review>());
        activity.populateFields(user);
        assertEquals("Name was not equal", user.getName(),
                ((EditText) activity.findViewById(R.id.editUserName)).getText().toString());
        assertEquals("Email was not equal",user.getEmail(),
                ((EditText) activity.findViewById(R.id.editUserEmail)).getText().toString());
        assertEquals("Address line 1 was not equal", user.getAddress1(),
                ((EditText) activity.findViewById(R.id.editUserAddress1)).getText().toString());
        assertEquals("Address Line 2 was not equal",user.getAddress2(),
                ((EditText) activity.findViewById(R.id.editUserAddress2)).getText().toString());
        assertEquals("City was not equal",user.getCity(),
                ((EditText) activity.findViewById(R.id.editUserCity)).getText().toString());
        assertEquals("Phone was not equal",user.getPhone(),
                ((EditText) activity.findViewById(R.id.editUserPhone)).getText().toString());
        assertEquals("Postal Code was not equal",user.getPostal(),
                ((EditText) activity.findViewById(R.id.editUserPostalCode)).getText().toString());
    }
}