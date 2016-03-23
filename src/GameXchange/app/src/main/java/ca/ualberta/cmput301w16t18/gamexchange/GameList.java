package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameList {
    private ArrayList<Game> games = new ArrayList<Game>();

    public boolean inList(Game game) {
        return games.contains(game);
    }

    public void add(Game game) {
        games.add(game);
    }

    public void addAll(GameList other) {
        games.addAll(other.getGames());
    }

// --Commented out by Inspection START (3/22/16 6:39 PM):
//    public void removeGame(Game game) {
//        games.remove(games.indexOf(game));
//    }
// --Commented out by Inspection STOP (3/22/16 6:39 PM)

    public void removeGame(String id) {
        for (Game game : games) {
            if (game.getId().equals(id)) {
                games.remove(game);
                return;
            }
        }
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void clear() {
        games.clear();
    }

// Implement cache and pull new data

}
