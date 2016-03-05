package ca.ualberta.cmput301w16t18.gamexchange;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 2/12/2016.
 */
public class GameList {
    ArrayList<Game> games = new ArrayList<Game>();

    public GameList(String type) {
        //create dummy data. TODO: remove this, add actual data.
        for(int i = 0; i < 1000; i++) {
            games.add(new Game((Integer.toString(i)), "Available", "Blockbuster Game " + i, "developer", "platform", new ArrayList<String>(), "description"));
        }
        /*
        if(type.equals(Constants.MY_GAMES)) {
            //Get My Games from Elastic Search

        } else if(type.equals(Constants.BORROWED_GAMES)) {
            //Get Borrowed Games from Elastic Search
        } else if(type.equals(Constants.WISH_LIST)) {
            //Get Wish List from Elastic Search
        }
        */
    }

    public GameList(String type, String searchString) {
        if(type.equals(Constants.SEARCH)) {
            //preform elastic search with searchString.
        }
    }

    public boolean inList(Game game) {
        return games.contains(game);
    }

    public void add(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(games.indexOf(game));
    }

    public ArrayList<Game> getGames() {
        return games;
    }

// Implement cache and pull new data

}
