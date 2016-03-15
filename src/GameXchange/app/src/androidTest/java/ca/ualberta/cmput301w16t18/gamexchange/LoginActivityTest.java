package ca.ualberta.cmput301w16t18.gamexchange;

import android.widget.TextView;

import junit.framework.TestCase;

import org.w3c.dom.Text;

/**
 * Created by Adam on 2016-03-14.
 */
public class LoginActivityTest extends TestCase {

    public void testOnWrongPassword() throws Exception {
        LoginActivity activity = new LoginActivity();
        activity.onWrongPassword();
        assertEquals("Error was not set for password field", "This password is incorrect", ((TextView) activity.findViewById(R.id.password)).getError());
    }

    public void testOnNewAccount() throws Exception {
        //To be completed on design decisions.
    }
}