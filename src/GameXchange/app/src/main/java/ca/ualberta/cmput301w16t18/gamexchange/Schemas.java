package ca.ualberta.cmput301w16t18.gamexchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 3/11/2016.
 */
public class Schemas {
    public static JSONObject getUserSchema(User user) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", user.getEmail());
            object.put("name", user.getName());
            object.put("passhash", user.getPasshash());
            object.put("address1", user.getAddress1());
            object.put("address2", user.getAddress2());
            object.put("city", user.getCity());
            object.put("phone", user.getPhone());
            object.put("postal", user.getPostal());
            object.put("owned_games", new JSONArray(user.getGames()));
            object.put("watchlist", new JSONArray(user.getWatchlist()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject getUserLoginSchema(String userEmail) {
        JSONObject query = new JSONObject();
        JSONObject term = new JSONObject();
        JSONObject email = new JSONObject();
        try {
            email.put("email", userEmail);
            term.put("match", email);
            query.put("query", term);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return query;
    }

    public static JSONObject getGameSchema(Game game) {
        JSONObject object = new JSONObject();

        try {
            object.put("status",game.getStatus());
            object.put("title", game.getTitle());
            object.put("developer",game.getDeveloper());
            object.put("platform",game.getPlatform());
            object.put("description",game.getDescription());
            object.put("genres", new JSONArray(game.getGenres()));
            object.put("picture",game.getPicture());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject getLongList() {
        JSONObject object = new JSONObject();

        try {
            object.put("size",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject getPictureSchema(String picture) {
        JSONObject object = new JSONObject();
        JSONObject doc = new JSONObject();

        try {
            doc.put("picture",picture);
            object.put("doc", doc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject getSpecificList(ArrayList<String> gameIDs) {
        JSONObject query = new JSONObject();
        JSONObject object = new JSONObject();
        JSONObject ids = new JSONObject();

        try {
            ids.put("type","games");
            ids.put("values", new JSONArray(gameIDs));
            object.put("ids", ids);
            query.put("query", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return query;
    }
}
