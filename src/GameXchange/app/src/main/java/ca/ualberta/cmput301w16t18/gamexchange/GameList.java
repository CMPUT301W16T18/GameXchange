package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Model for list of game objects
 */
public class GameList {
    private ArrayList<Game> games = new ArrayList<>();

    public boolean inList(Game game) {
        return games.contains(game);
    }

    public void add(Game game) {
        games.add(game);
    }

    public void addAll(GameList other) {
        games.addAll(other.getGames());
    }

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

    public int size() { return games.size(); }

// Implement cache and pull new data

}
