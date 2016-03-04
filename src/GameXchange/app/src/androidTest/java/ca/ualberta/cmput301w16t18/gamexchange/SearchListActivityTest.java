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
        activity.addGame(test, "USER-ID");

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

    public void testLoadOwnedGames() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        activity.loadOwnedGames("USER-ID");
        Game test = new Game(); //Game that this user definitely owns
        activity.addGame(test, "USER-ID");

        assertTrue(activity.games.inList(test));
    }

    public void testLoadBorrowingGames() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        activity.loadBorrowingGames("USER-ID");
        Game test = new Game(); //Game that this user is borrowing
        activity.addGame(test, "USER-ID");

        assertTrue(activity.games.inList(test));
    }

    public void testLoadBorrowedGames() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        activity.loadBorrowedGames("USER-ID");
        Game test = new Game(); //Game that this user has lent out
        activity.addGame(test, "USER-ID");

        assertTrue(activity.games.inList(test));
    }

    public void testSearchGames() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        activity.searchGames("Search String");
        Game test = new Game(); //Game that should appear on this list
        activity.addGame(test, "USER-ID");

        assertTrue(activity.games.inList(test));
    }

    public void testAddToWatchlist() throws Exception {
        SearchListActivity activity = new SearchListActivity ();
        Game test = new Game(); //Game that should appear on the user's watchlist.
        activity.addToWatchlist(test);

        assertTrue(activity.games.inList(test));
    }
}
