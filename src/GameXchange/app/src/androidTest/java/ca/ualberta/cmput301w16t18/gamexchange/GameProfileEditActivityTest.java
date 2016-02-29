package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameProfileEditActivityTest extends TestCase {

    public void testEditGame() throws Exception {
        GameProfileEditActivity activity = new GameProfileEditActivity();
        Game test = new Game();
        test.setName("Title1");

        assertEquals(test.getName(), "Title2");
    }

    public void testCacheGame() throws Exception {
        GameProfileEditActivity activity = new GameProfileEditActivity();
        Game test = new Game();
        test.setName("Title1");
    }
}