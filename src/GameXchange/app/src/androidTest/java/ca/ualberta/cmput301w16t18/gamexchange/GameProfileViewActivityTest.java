package ca.ualberta.cmput301w16t18.gamexchange;

import junit.framework.TestCase;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameProfileViewActivityTest extends TestCase {

    public void testLoadGame() throws Exception {
        GameProfileViewActivity activity = new GameProfileViewActivity ();
        activity.loadGame("GAME-ID");
        assertEquals(activity.game.getName(), "Game Name");
    }
}