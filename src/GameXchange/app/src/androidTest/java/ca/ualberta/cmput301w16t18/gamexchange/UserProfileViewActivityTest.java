package ca.ualberta.cmput301w16t18.gamexchange;

import android.widget.TextView;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class UserProfileViewActivityTest extends TestCase {

    // Need to work out how to test in JUnit
    /*
    public void testLoadUser() throws Exception {
        UserProfileViewActivity activity = new UserProfileViewActivity ();
        User testUser = new User();
        testUser.setName("John");
        //activity.loadUser(testUser.getID());
        
        assertEquals(activity.user.getName(), testUser.getName());
    }
    */
    
    public void testPopulateFields() {
        UserProfileViewActivity activity = new UserProfileViewActivity();
        User user = new User("TEST_ID","Email","Name","Passhash","Address1","Address2","City",
                "Phone","postal",new ArrayList<String>(),new ArrayList<String>());
        activity.populateFields(user);
        assertEquals("Name was not equal", user.getName(),
                ((TextView) activity.findViewById(R.id.viewUserName)).getText().toString());
        assertEquals("Email was not equal",user.getEmail(),
                ((TextView) activity.findViewById(R.id.viewUserEmail)).getText().toString());
        assertEquals("Address line 1 was not equal", user.getAddress1(),
                ((TextView) activity.findViewById(R.id.viewUserAddress1)).getText().toString());
        assertEquals("Address Line 2 was not equal",user.getAddress2(),
                ((TextView) activity.findViewById(R.id.viewUserAddress2)).getText().toString());
        assertEquals("City was not equal",user.getCity(),
                ((TextView) activity.findViewById(R.id.viewUserCity)).getText().toString());
        assertEquals("Phone was not equal",user.getPhone(),
                ((TextView) activity.findViewById(R.id.viewUserPhone)).getText().toString());
        assertEquals("Postal Code was not equal",user.getPostal(),
                ((TextView) activity.findViewById(R.id.viewUserPostalCode)).getText().toString());    
    }

    /* No need to test, just sends an intent.
    public void testEditUserProfile() {

    }
     */
    
}
