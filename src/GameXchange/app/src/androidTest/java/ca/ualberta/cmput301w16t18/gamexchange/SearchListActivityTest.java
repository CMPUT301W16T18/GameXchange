package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class SearchListActivityTest extends TestCase {

    public void testAddGame() throws Exception {
        Game test = new Game();
        //test.setName("Test Game");

        SearchListActivity activity = new SearchListActivity ();
        activity.addGame(test);

        assertTrue(activity.games.inList(test));

    }

    public void testDeleteGame() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        Game test = new Game();
        //test.setName("Title1","USER-ID");
        //activity.addGame(test, "USER-ID");
        //activity.deleteGame(test);

        assertFalse(activity.games.inList(test));
    }
}
