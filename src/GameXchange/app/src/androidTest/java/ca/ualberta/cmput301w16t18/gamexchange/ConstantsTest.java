package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Adam on 2016-03-14.
 */
public class ConstantsTest extends TestCase {

    public void testGetPrefix() throws Exception {
        assertEquals("Prefix was not equal",Constants.serverURL + Constants.index + "/", Constants.getPrefix());
    }

    public void testIsEmailValid() throws Exception {
        String email = "abc@def.com";
        assertTrue(Constants.isEmailValid(email));
        email = "abc.com";
        assertFalse(Constants.isEmailValid(email));
    }

    public void testIsPasswordValid() throws Exception {
        String password = "abcdef";
        assertTrue(Constants.isPasswordValid(password));
        password = "ab";
        assertFalse(Constants.isPasswordValid(password));
    }
}