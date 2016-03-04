package ca.ualberta.cmput301w16t18.gamexchange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class Constants {
    public static String serverURL = "http://elastic.vassi.li:9200/"; // TODO: Change this later
    public static String index = "gamexchange";

    public static String getPrefix() { return serverURL + index + "/"; }

    public static String GAME_ID = "GAME_ID";
    public static String USER_ID = "USER_ID";
    public static int REQUEST_IMAGE_CAPTURE = 1;

    public static int SWIPE_MIN_DISTANCE = 300;
    public static int SWIPE_THRESHOLD_VELOCITY = 150;

    public static Boolean DEBUG = true;

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

    public static JSONObject getGameSchema(Game game) {
        JSONObject object = new JSONObject();

        try {
            object.put("status",game.getStatus());
            object.put("name", game.getTitle());
            object.put("developer",game.getDeveloper());
            object.put("platform",game.getPlatform());
            object.put("description",game.getDescription());
            object.put("genres", new JSONArray(game.getGenres()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
