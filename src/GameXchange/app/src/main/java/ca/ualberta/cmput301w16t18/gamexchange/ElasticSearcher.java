package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class enables network connectivity to the elastic search backend
 * Allows us to transmit and receive data describing users and games
 * and also handles pictures
 */
class ElasticSearcher {

    private static final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERROR", "Whoops. I couldn't do the thing.");
            error.printStackTrace();
        }
    };

    private static final Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            /*Log.i("RESPONSE", response.toString());*/
        }
    };

    /**
     * Sends a game to elastic search
     * @param game the game to be sent
     */
    public static void sendGame(final Game game) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (game.getId().equals("")) {
                    try {
                        String id = response.getString("_id");
                        addGameToList(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/" + game.getId(),
                Schemas.gameSchema(game), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * sends a user to elastic search
     * @param user the user to be sent
     */
    public static void sendUser(final User user) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "users/" + user.getId(),
                Schemas.userSchema(user), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * adds a game to a users list depending on context
     * @param gameID the game to be added to the list
     */
    public static void addGameToList(String gameID) {
        User user = Constants.CURRENT_USER;
        if (Constants.SEARCHLIST_CONTEXT.equals(Constants.MY_GAMES)) {
            ArrayList myGames = user.getGames();
            myGames.add(gameID);
            user.setGames(myGames);
        }
        else if (Constants.SEARCHLIST_CONTEXT.equals(Constants.WATCH_LIST)) {
            ArrayList myGames = user.getWatchlist();
            myGames.add(gameID);
            user.setWatchlist(myGames);
        }
        sendUser(user);
    }

    /**
     * removes game from a users list depending on context
     * @param gameID the game to be removed
     */
    public static void removeGameFromList(String gameID) {
        User user = Constants.CURRENT_USER;
        if (Constants.SEARCHLIST_CONTEXT.equals(Constants.MY_GAMES)) {
            ArrayList myGames = user.getGames();
            myGames.remove(gameID);
            user.setGames(myGames);
        }
        else if (Constants.SEARCHLIST_CONTEXT.equals(Constants.WATCH_LIST)) {
            ArrayList myGames = user.getWatchlist();
            myGames.remove(gameID);
            user.setWatchlist(myGames);
        }
        sendUser(user);
    }

    /**
     * recieves a game from elastic search and sends it to the calling activity
     * @param id the game id
     * @param activity calling activity
     */
    public static void receiveGame(final String id, final Activity activity) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Game game = responseToGame(response);

                if (activity.getLocalClassName().equals("GameProfileViewActivity")) {
                    GameProfileViewActivity other = (GameProfileViewActivity) activity;
                    other.populateFields(game);
                }
                else if (activity.getLocalClassName().equals("GameProfileEditActivity")) {
                    GameProfileEditActivity other = (GameProfileEditActivity) activity;
                    other.setGame(game);
                    other.populateFields(game);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.getPrefix() + "games/" + id,
                new JSONObject(), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * recieves a user from elastic search and sends to calling activity
     * @param id users id
     * @param activity calling activity
     */
    public static void receiveUser(final String id, final Activity activity) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                User user = responseToUser(response);

                if (activity.getLocalClassName().equals("UserProfileViewActivity")) {
                    UserProfileViewActivity other = (UserProfileViewActivity) activity;
                    other.populateFields(user);
                }
                else if (activity.getLocalClassName().equals("UserProfileEditActivity")) {
                    UserProfileEditActivity other = (UserProfileEditActivity) activity;
                    other.setUser(user);
                    other.populateFields(user);
                }
                else if (activity.getLocalClassName().equals("GameProfileViewActivity")) {
                    GameProfileViewActivity other = (GameProfileViewActivity) activity;
                    other.elasticSearcherCallback(user);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.getPrefix() + "users/" + id,
                new JSONObject(), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * Deletes a game from elastic search
     * @param id the game id
     * @param activity calling activity
     */
    public static void deleteGame(final String id, final Activity activity) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SearchListActivity searchListActivity = (SearchListActivity) activity;
                searchListActivity.deleteGame(id);
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE,
                Constants.getPrefix() + "games/" + id,
                new JSONObject(), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * recieves a list of games from elastic search depending on context
     * @param activity calling activity
     */
    public static void receiveGames(final Activity activity) {
        final String which = Constants.SEARCHLIST_CONTEXT;

        if (which.equals(Constants.ALL_GAMES)) {
            receiveAllGames(activity);
            return;
        }

        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ArrayList<String> gameIDs = new ArrayList<>();

                try {
                    JSONObject source = response.getJSONObject("_source");
                    JSONArray gamesList = new JSONArray();
                    switch (which) {
                        case Constants.MY_GAMES:
                            gamesList = source.getJSONArray("owned_games");
                            break;
                        case Constants.WATCH_LIST:
                            gamesList = source.getJSONArray("watchlist");
                            break;
                        case Constants.BORROWED_GAMES:
                            //TODO: remove line when database cleared
                            if (source.has("borrowing_games"))
                                gamesList = source.getJSONArray("borrowing_games");
                            break;
                    }

                    for (int i=0; i<gamesList.length(); i++) {
                        gameIDs.add(gamesList.get(i).toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                receiveListOfGames(gameIDs, activity);
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.getPrefix() + "users/" + Constants.CURRENT_USER.getId(),
                new JSONObject(), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    private static void receiveAllGames(final Activity activity) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);

                if (activity.getLocalClassName().equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(games);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.longListSchema(), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    private static void receiveListOfGames(final ArrayList<String> gameList, final Activity activity) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);

                if (activity.getLocalClassName().equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(games);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.specificListSchema(gameList), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * recieves games from elastic search by a search term
     * @param search the term to search for
     * @param activity calling activity
     */
    public static void receiveGamesBySearchTerm(final String search, final Activity activity) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);

                if (activity.getLocalClassName().equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(games);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.textSearchSchema(search), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * Authenticates user on login by comparing the email and password hash to database contents
     * @param email the users email
     * @param passhash the hash of the users password
     * @param loginActivity calling activity
     */
    public static void authenticateUser(final String email, final String passhash, final LoginActivity loginActivity) {
        Response.Listener<JSONObject> loginListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject hits = response.getJSONObject("hits");
                    JSONArray hitsArray = hits.getJSONArray("hits");

                    for (int i = 0; i < hitsArray.length(); i++) {
                        JSONObject hit = hitsArray.getJSONObject(i);
                        User user = responseToUser(hit);

                        if (email.equals( user.getEmail() )) {
                            if (passhash.equals( user.getPasshash() )) {
                                Constants.CURRENT_USER = user;
                                loginActivity.onLoginSuccess();
                            }
                            else {
                                loginActivity.onWrongPassword();
                            }
                            return;
                        }
                    }

                    loginActivity.onNewAccount();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "users/_search",
                Schemas.userLoginSchema(email), loginListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * Generates a list of all notifications that the user should see
     * @param activity calling activity
     */
    public static void getNotifications(final Activity activity) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);
                ArrayList<Game> gameslist = games.getGames();

                for (Game i : gameslist) {
                    i.notification = "Your bid was accepted.";
                }

                getWatchlistChanges(activity, games);
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.myAcceptedBidsSchema(), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    private static void getWatchlistChanges(final Activity activity, final GameList all_games) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);
                ArrayList<Game> gameslist = games.getGames();

                for (Game i : gameslist) {
                    i.notification = "A game on your watchlist became available.";
                }

                all_games.addAll(games);

                getNewBids(activity, all_games);
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.myWatchlistChangesSchema(), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    private static void getNewBids(final Activity activity, final GameList all_games) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);
                ArrayList<Game> gameslist = games.getGames();

                for (Game i : gameslist) {
                    i.notification = "Someone placed a bid on your game.";
                }

                all_games.addAll(games);

                if (activity.getLocalClassName().equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(all_games);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.myNewBidsSchema(), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * recieves a list of games that the current user has bid on but were still pending
     * @param activity calling activity
     */
    public static void receiveMyBids(final Activity activity) {
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = responseToGameList(response);

                if (activity.getLocalClassName().equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(games);
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                Schemas.myBidsSchema(), responseListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * retrieves the user who owns a specific game
     * @param activity calling activity
     * @param gameID the game id
     */
    public static void showOwner(final Activity activity, final String gameID) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<User> user = responseToUserList(response);
                if (activity.getLocalClassName().equals("GameProfileViewActivity")) {
                    GameProfileViewActivity gameProfileViewActivity = (GameProfileViewActivity) activity;
                    gameProfileViewActivity.openUserProfile(user.get(0).getId());
                }
            }
        };
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.getPrefix() + "users/_search",
                Schemas.ownerSchema(gameID), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    /**
     * receives user who is borrowing a specific game
     * @param activity calling activity
     * @param gameID the game id
     */
    public static void getBorrowingUser(final GameProfileViewActivity activity, String gameID) {
        Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ArrayList<User> user = responseToUserList(response);
                activity.saveReviewAndRemoveBorrowedGame(user.get(0));
            }
        };
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.getPrefix() + "users/_search",
                Schemas.borrowerSchema(gameID), jsonListener, errorListener);
        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

    private static User responseToUser(JSONObject response) {
        User user = new User();
        JSONArray borrowingGamesList = new JSONArray(); //TODO: remove this when database is reset
        JSONArray reviewsList = new JSONArray(); //TODO: remove this when database is reset

        try {
            String id = response.getString("_id");
            JSONObject source = response.getJSONObject("_source");
            String email = source.getString("email");
            String name = source.getString("name");
            String passhash = source.getString("passhash");
            String address1 = source.getString("address1");
            String address2 = source.getString("address2");
            String city = source.getString("city");
            String phone = source.getString("phone");
            String postal = source.getString("postal");
            JSONArray ownedGamesList = source.getJSONArray("owned_games");
            ArrayList<String> owned_games = new ArrayList<>();
            for (int i=0; i<ownedGamesList.length(); i++) {
                owned_games.add(ownedGamesList.get(i).toString());
            }
            JSONArray watchedGamesList = source.getJSONArray("watchlist");
            ArrayList<String> watchlist = new ArrayList<>();
            for (int i=0; i<watchedGamesList.length(); i++) {
                watchlist.add(watchedGamesList.get(i).toString());
            }
            if (source.has("borrowing_games")) {
                borrowingGamesList = source.getJSONArray("borrowing_games");
            }
            ArrayList<String> borrowing_games = new ArrayList<>();
            for (int i=0; i<borrowingGamesList.length(); i++) {
                borrowing_games.add(borrowingGamesList.get(i).toString());
            }
            if (source.has("reviews")) {
                reviewsList = source.getJSONArray("reviews");
            }
            ArrayList<Review> reviews = responseToReviewList(reviewsList);

            user = new User(id, email, name, passhash, address1, address2, city, phone, postal, owned_games, watchlist, borrowing_games, reviews);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private static Game responseToGame(JSONObject response) {
        Game game = new Game();
        String picture = ""; //TODO: remove this when database is reset
        JSONArray bidsList = new JSONArray(); //TODO: remove this when database is reset

        try {
            String id = response.getString("_id");
            JSONObject source = response.getJSONObject("_source");
            String status = source.getString("status");
            String title = source.getString("title");
            String developer = source.getString("developer");
            String platform = source.getString("platform");
            String description = source.getString("description");
            if (source.has("picture")) {
                picture = source.getString("picture");
            }
            JSONArray genresList = source.getJSONArray("genres");
            ArrayList<String> genres = new ArrayList<>();
            for (int i=0; i<genresList.length(); i++) {
                genres.add(genresList.get(i).toString());
            }
            if (source.has("bids")) {
                bidsList = source.getJSONArray("bids");
            }
            ArrayList<Bid> bids = responseToBidList(bidsList);

            game = new Game(id, status, title, developer, platform, genres, description, picture, bids);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return game;
    }

    private static GameList responseToGameList(JSONObject response) {
        GameList games = new GameList();
        try {
            JSONObject hits = response.getJSONObject("hits");
            JSONArray hitsArray = hits.getJSONArray("hits");

            for (int i = 0; i < hitsArray.length(); i++) {
                JSONObject hit = hitsArray.getJSONObject(i);
                Game game = responseToGame(hit);
                games.add(game);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return games;
    }

    private static ArrayList<Review> responseToReviewList(JSONArray response) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject reviewJSON = response.getJSONObject(i);
                Review review = responseToReview(reviewJSON);
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    private static Review responseToReview(JSONObject response) {
        Review review = new Review();
        try {
            long timestamp = response.getLong("timestamp");
            String reviewBody = response.getString("reviewBody");
            float rating = (float) response.getDouble("rating");
            String reviewer = response.getString("reviewer");
            String gameID = response.getString("gameID");

            review = new Review(timestamp, reviewBody, rating, reviewer, gameID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return review;
    }

    private static ArrayList<Bid> responseToBidList(JSONArray response) {
        ArrayList<Bid> bids = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject bidJSON = response.getJSONObject(i);
                Bid bid = responseToBid(bidJSON);
                bids.add(bid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bids;
    }

    private static Bid responseToBid(JSONObject response) {
        Bid bid = new Bid();
        try {
            String bidder = response.getString("bidder");
            double price = (float) response.getDouble("price");
            double latitude = response.getDouble("latitude");
            double longitude = response.getDouble("longitude");
            String status = response.getString("status");

            bid = new Bid(bidder, price, latitude, longitude, status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bid;
    }

    private static ArrayList<User> responseToUserList(JSONObject response) {
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONObject hits = response.getJSONObject("hits");
            JSONArray hitsArray = hits.getJSONArray("hits");

            for (int i = 0; i < hitsArray.length(); i++) {
                JSONObject hit = hitsArray.getJSONObject(i);
                User user = responseToUser(hit);
                users.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }
}
