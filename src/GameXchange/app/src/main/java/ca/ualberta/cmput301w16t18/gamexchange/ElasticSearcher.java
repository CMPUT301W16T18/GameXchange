package ca.ualberta.cmput301w16t18.gamexchange;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class ElasticSearcher {
    private static RequestQueue queue;

    private static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("ERROR", "Whoops. I couldn't do the thing.");
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

    public static void receiveGame(String id, final Activity activity, final String activityName) {
        queue = Volley.newRequestQueue(activity);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Parser parser = new Parser(response);
                Game game = new Game(parser.getStringValue("_id"),
                        parser.getStringValue("status"),
                        parser.getStringValue("title"),
                        parser.getStringValue("developer"),
                        parser.getStringValue("platform"),
                        parser.getArrayValue("genres"),
                        parser.getStringValue("description"),
                        parser.getStringValue("owner"));
                // TODO: Do stuff with game
                Log.i("RESPONSE", response);
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
                    loadUserIntoViewActivity(user, activity);
                }
                else if (activityName.equals("UserProfileEditActivity")) {
                    UserProfileEditActivity other = (UserProfileEditActivity) activity;
                    other.setUser(user);
                    loadUserIntoEditActivity(user, activity);
                }
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.getPrefix() + "users/" + id, responseListener, errorListener);

        queue.add(stringRequest);
    }

    private static void loadUserIntoViewActivity(User user, Activity activity) {
        TextView viewUserName = (TextView) activity.findViewById(R.id.viewUserName);
        TextView viewUserEmail = (TextView) activity.findViewById(R.id.viewUserEmail);
        TextView viewUserAddress1 = (TextView) activity.findViewById(R.id.viewUserAddress1);
        TextView viewUserAddress2 = (TextView) activity.findViewById(R.id.viewUserAddress2);
        TextView viewUserCity = (TextView) activity.findViewById(R.id.viewUserCity);
        TextView viewUserPhone = (TextView) activity.findViewById(R.id.viewUserPhone);
        TextView viewUserPostalCode = (TextView) activity.findViewById(R.id.viewUserPostalCode);

        viewUserName.setText(user.getName());
        viewUserEmail.setText(user.getEmail());
        viewUserAddress1.setText(user.getAddress1());
        viewUserAddress2.setText(user.getAddress2());
        viewUserCity.setText(user.getCity());
        viewUserPhone.setText(user.getPhone());
        viewUserPostalCode.setText(user.getPostal());
    }

    private static void loadUserIntoEditActivity(User user, Activity activity) {
        EditText editUserName = (EditText) activity.findViewById(R.id.editUserName);
        EditText editUserEmail = (EditText) activity.findViewById(R.id.editUserEmail);
        EditText editUserAddress1 = (EditText) activity.findViewById(R.id.editUserAddress1);
        EditText editUserAddress2 = (EditText) activity.findViewById(R.id.editUserAddress2);
        EditText editUserCity = (EditText) activity.findViewById(R.id.editUserCity);
        EditText editUserPhone = (EditText) activity.findViewById(R.id.editUserPhone);
        EditText editUserPostalCode = (EditText) activity.findViewById(R.id.editUserPostalCode);

        editUserName.setText(user.getName());
        editUserEmail.setText(user.getEmail());
        editUserAddress1.setText(user.getAddress1());
        editUserAddress2.setText(user.getAddress2());
        editUserCity.setText(user.getCity());
        editUserPhone.setText(user.getPhone());
        editUserPostalCode.setText(user.getPostal());
    }
}
