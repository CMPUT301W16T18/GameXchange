package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameList {
    private ArrayList<Game> games;

    //Array functions here
    public void add(Game game) {
        games.add(game);
    }

    public boolean inList(Game game) {
        return games.contains(game);
    }
}
