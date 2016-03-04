package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;

import junit.framework.TestCase;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class UserProfileViewActivityTest extends TestCase {

    public void testLoadUser() throws Exception {
        UserProfileViewActivity activity = new UserProfileViewActivity ();
        User testUser = new User();
        testUser.setName("John");
        //activity.loadUser(testUser.getID());
        
        assertEquals(activity.user.getName(), testUser.getName());
    }
}
