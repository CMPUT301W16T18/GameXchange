package ca.ualberta.cmput301w16t18.gamexchange;

import android.media.Image;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameProfileViewActivityTest extends TestCase {

    /* not sure how to test without network connectivity
    public void testLoadGame() throws Exception {
        GameProfileViewActivity activity = new GameProfileViewActivity ();
        activity.loadGame("GAME-ID");
        //assertEquals(activity.game.getName(), "Game Name");
    }
    */

    public void testPopulateFields() {
        ArrayList<String> genres = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        GameProfileViewActivity activity = new GameProfileViewActivity();
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);
        activity.populateFields(game);
        assertEquals("Title was not equal", game.getTitle(),
                ((TextView) activity.findViewById(R.id.game_view_title)).getText().toString());
        assertEquals("Developer was not equal", game.getDeveloper(),
                ((TextView) activity.findViewById(R.id.game_view_developer)).getText().toString());
        assertEquals("Platform was not equal", game.getPlatform(),
                ((TextView) activity.findViewById(R.id.game_view_platform)).getText().toString());
        assertEquals("Genres were not equal", TextUtils.join(", ", game.getGenres()),
                ((TextView) activity.findViewById(R.id.game_view_genres)).getText().toString());
        assertEquals("Description was not equal", game.getDescription(),
                ((TextView) activity.findViewById(R.id.game_view_description)).getText().toString());
    }

    public void testEditGameProfile() {
        // No tests needed, just calling an intent.
    }
}