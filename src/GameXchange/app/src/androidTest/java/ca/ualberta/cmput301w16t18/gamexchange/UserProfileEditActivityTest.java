package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class UserProfileEditActivityTest extends TestCase {

    public void testEditUser() throws Exception {
        UserProfileEditActivity activity = new UserProfileEditActivity();
        User test = new User();
        test.setName("John");
        //activity.editUser(test);

        assertEquals(test.getName(), "Bill");
    }
}