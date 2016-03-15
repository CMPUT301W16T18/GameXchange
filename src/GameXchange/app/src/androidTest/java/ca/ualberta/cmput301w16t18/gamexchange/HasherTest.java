package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Adam on 2016-03-14.
 */
public class HasherTest extends TestCase {

    public void testGetHash() throws Exception {
        String passhash = "AVM1KtaDI8oCfzIHasfN";
        String compare = Hasher.getHash("password");
        assertEquals("Password hashes were not equal",passhash,compare);
    }
}