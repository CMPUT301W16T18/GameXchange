package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class ElasticSearcher {
    private static RequestQueue queue;

    private static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERROR", "Whoops. I couldn't do the thing.");
        }
    };

    private static Response.Listener<JSONObject> jsonListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            /*Log.i("RESPONSE", response.toString());*/
        }
    };

    public static void sendGame(final Game game, Context context) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/" + game.getId(),
                Constants.getGameSchema(game), jsonListener, errorListener);

        queue.add(jsonRequest);
    }

    public static void sendUser(final User user, Context context) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "users/" + user.getId(),
                Constants.getUserSchema(user), jsonListener, errorListener);

        queue.add(jsonRequest);
    }

    public static void receiveGame(final String id, final Activity activity, final String activityName) {
        queue = Volley.newRequestQueue(activity);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Parser parser = new Parser(response);
                Game game = new Game(id,
                        parser.getStringValue("status"),
                        parser.getStringValue("title"),
                        parser.getStringValue("developer"),
                        parser.getStringValue("platform"),
                        parser.getArrayValue("genres"),
                        parser.getStringValue("description"));

                if (activityName.equals("GameProfileViewActivity")) {
                    GameProfileViewActivity other = (GameProfileViewActivity) activity;
                    other.populateFields(game);
                }
                else if (activityName.equals("GameProfileEditActivity")) {
                    GameProfileEditActivity other = (GameProfileEditActivity) activity;
                    other.setGame(game);
                    other.populateFields(game);
                }
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.getPrefix() + "games/" + id, responseListener, errorListener);

        queue.add(stringRequest);
    }

    public static void receiveUser(String id, final Activity activity, final String activityName) {
        queue = Volley.newRequestQueue(activity);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Parser parser = new Parser(response);
                User user = new User(parser.getStringValue("_id"),
                        parser.getStringValue("email"),
                        parser.getStringValue("name"),
                        parser.getStringValue("passhash"),
                        parser.getStringValue("address1"),
                        parser.getStringValue("address2"),
                        parser.getStringValue("city"),
                        parser.getStringValue("phone"),
                        parser.getStringValue("postal"),
                        parser.getArrayValue("owned_games"),
                        parser.getArrayValue("watchlist"));

                if (activityName.equals("UserProfileViewActivity")) {
                    UserProfileViewActivity other = (UserProfileViewActivity) activity;
                    other.populateFields(user);
                }
                else if (activityName.equals("UserProfileEditActivity")) {
                    UserProfileEditActivity other = (UserProfileEditActivity) activity;
                    other.setUser(user);
                    other.populateFields(user);
                }
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.getPrefix() + "users/" + id, responseListener, errorListener);

        queue.add(stringRequest);
    }

    public static void receiveAllGames(final Activity activity, final String activityName) {
        queue = Volley.newRequestQueue(activity);

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GameList games = new GameList();
                try {
                    JSONObject hits = response.getJSONObject("hits");
                    JSONArray hitsArray = hits.getJSONArray("hits");

                    for (int i = 0; i < hitsArray.length(); i++) {
                        JSONObject hit = hitsArray.getJSONObject(i);
                        Parser parser = new Parser(hit.toString());
                        Game game = new Game(parser.getStringValue("_id"),
                                parser.getStringValue("status"),
                                parser.getStringValue("title"),
                                parser.getStringValue("developer"),
                                parser.getStringValue("platform"),
                                parser.getArrayValue("genres"),
                                parser.getStringValue("description"));
                        games.add(game);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (activityName.equals("SearchListActivity")) {
                    SearchListActivity searchListActivity = (SearchListActivity) activity;
                    searchListActivity.setDisplayedList(games);
                }
                else {
                    //TODO: Are we going to call this method from anywhere else? Probably not.
                }
            }
        };

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Constants.getPrefix() + "games/_search",
                new JSONObject(), responseListener, errorListener);

        queue.add(jsonRequest);
    }

    public static void authenticateUser(final String email, final String passhash, final LoginActivity loginActivity) {
        queue = Volley.newRequestQueue(loginActivity);

        Response.Listener<JSONObject> loginListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject hits = response.getJSONObject("hits");
                    JSONArray hitsArray = hits.getJSONArray("hits");

                    for (int i = 0; i < hitsArray.length(); i++) {
                        JSONObject hit = hitsArray.getJSONObject(i);
                        JSONObject source = hit.getJSONObject("_source");

                        if (email.equals( source.get("email").toString() )) {
                            if (passhash.equals( source.get("passhash").toString() )) {
                                Constants.CURRENT_USER = hit.getString("_id");
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
                Constants.getUserLoginSchema(email), loginListener, errorListener);

        queue.add(jsonRequest);
    }

}
