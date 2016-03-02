package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class ElasticSearcher {
    private static RequestQueue queue;

    public static void sendGame(final Game game, Context context) {
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                Constants.getPrefix() + "games/",
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.i("RESPONSE", response); }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "Whoops. I couldn't do the thing.");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", game.getId());
                params.put("status",game.getStatus());
                params.put("name", game.getName());
                params.put("developer",game.getDeveloper());
                params.put("platform",game.getPlatform());
                params.put("genres",game.getGenres().toString());
                params.put("description",game.getDescription());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public static void sendUser (final User user, Context context) {
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                Constants.getPrefix() + "users/",
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.i("RESPONSE", response); }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "Whoops. I couldn't do the thing.");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", user.getId());
                params.put("email",user.getId());
                params.put("name", user.getName());
                params.put("passhash",user.getId());
                params.put("address1",user.getId());
                params.put("address2",user.getId());
                params.put("city",user.getId());
                params.put("phone",user.getId());
                params.put("postal",user.getId());
                params.put("owned_games",user.getId());
                params.put("watchlist",user.getId());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static Game receiveGame(String id, Context context) {
        queue = Volley.newRequestQueue(context);
        final Game[] game = {new Game()};

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.getPrefix() + "games/" + id,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<Game>() {}.getType();
                game[0] = gson.fromJson(response, type);
                Log.i("RESPONSE", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "Whoops. I couldn't do the thing.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return game[0];
    }

    public static GameList receiveGamesForUser(String id, Context context) {
        queue = Volley.newRequestQueue(context);
        GameList games = new GameList();
        Game game;
        User user = receiveUser(id, context);

        for (String gameID : user.getGames()) {
            game = receiveGame(gameID, context);
            games.add(game);
        }

        return games;
    }

    public static User receiveUser(String id, Context context) {
        queue = Volley.newRequestQueue(context);
        final User[] user = {new User()};

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.getPrefix() + "users/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<User>() {}.getType();
                        user[0] = gson.fromJson(response, type);
                        Log.i("RESPONSE", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "Whoops. I couldn't do the thing.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return user[0];
    }
}
