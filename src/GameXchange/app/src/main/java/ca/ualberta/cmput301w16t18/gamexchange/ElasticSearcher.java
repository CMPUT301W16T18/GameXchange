package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;
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
 * Created by Vassili Minaev on 2/29/2016.
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

    public static void sendUser(final User user) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "users/" + user.getId(),
                Schemas.userSchema(user), jsonListener, errorListener);

        NetworkSingleton.getInstance().addToRequestQueue(jsonRequest);
    }

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

    public static void getNotifications(final Activity activity) {
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

    //TODO: Fix this, it returns all users at the moment. the schema should be right though.
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
