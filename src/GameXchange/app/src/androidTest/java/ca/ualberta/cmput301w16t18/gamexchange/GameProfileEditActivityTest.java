package ca.ualberta.cmput301w16t18.gamexchange;

import android.text.TextUtils;
import android.widget.EditText;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-03-14.
 */
public class GameProfileEditActivityTest extends TestCase {

    /* Not sure how to test without network activity
    public void testLoadGame() throws Exception {

    }
    */


    public void testPopulateFields() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        GameProfileEditActivity activity = new GameProfileEditActivity();
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture", bids);
        activity.populateFields(game);
        assertEquals("Title was not equal", game.getTitle(),
                ((EditText) activity.findViewById(R.id.game_edit_title)).getText().toString());
        assertEquals("Developer was not equal", game.getDeveloper(),
                ((EditText) activity.findViewById(R.id.game_edit_developer)).getText().toString());
        assertEquals("Platform was not equal", game.getPlatform(),
                ((EditText) activity.findViewById(R.id.game_edit_platform)).getText().toString());
        assertEquals("Genres were not equal", TextUtils.join(", ", game.getGenres()),
                ((EditText) activity.findViewById(R.id.game_edit_genres)).getText().toString());
        assertEquals("Description was not equal", game.getDescription(),
                ((EditText) activity.findViewById(R.id.game_edit_description)).getText().toString());
    }

    public void testGetGame() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture", bids);
        GameProfileEditActivity activity = new GameProfileEditActivity();
        activity.setGame(game);
        assertEquals("Game was not equal",game,activity.getGame());
    }

    public void testSetGame() throws Exception {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture", bids);
        GameProfileEditActivity activity = new GameProfileEditActivity();
        activity.setGame(game);
        assertEquals("Game was not equal",game,activity.getGame());
    }

    // Testing to be implemented
    public void testEditGame() throws Exception {

    }

    // testing to be implemented
    public void testCacheGame() throws Exception {

    }
}