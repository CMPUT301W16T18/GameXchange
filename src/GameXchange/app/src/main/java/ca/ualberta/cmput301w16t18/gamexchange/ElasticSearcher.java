package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class ElasticSearcher {
    private static RequestQueue queue;

    private static void sendQuery(String suffix, Context context) {
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                Constants.getPrefix() + suffix, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.i("RESPONSE", response); }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", "Whoops. I couldn't do the thing.");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void sendGame(Game game, Context context) {
        String suffix = "games/";

        // TODO: Actually make the proper URL here
        sendQuery(suffix, context);
    }
    public static void sendUser (User user, Context context) {
        String suffix = "users/";

        // TODO: Actually make the proper URL here
        sendQuery(suffix, context);
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
