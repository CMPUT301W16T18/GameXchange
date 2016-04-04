package ca.ualberta.cmput301w16t18.gamexchange;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vassili Minaev on 3/11/2016.
 */
class Schemas {

    public static JSONObject userSchema(User user) {
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
            object.put("borrowing_games", new JSONArray(user.getBorrowing()));
            object.put("reviews", reviewsSchema(user.getReviews()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject userLoginSchema(String userEmail) {
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

    public static JSONObject gameSchema(Game game) {
        JSONObject object = new JSONObject();

        try {
            object.put("status",game.getStatus());
            object.put("title", game.getTitle());
            object.put("developer",game.getDeveloper());
            object.put("platform",game.getPlatform());
            object.put("description", game.getDescription());
            object.put("genres", new JSONArray(game.getGenres()));
            object.put("picture", game.getPicture());
            object.put("bids", bidsSchema(game.getBids()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Troubleshooting", "Game Object: " + object.toString());
        return object;
    }

    public static JSONObject longListSchema() {
        JSONObject object = new JSONObject();

        try {
            object.put("size",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject specificListSchema(ArrayList<String> gameIDs) {
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

    public static JSONObject textSearchSchema(String search) {
        JSONObject rv = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject multi_match = new JSONObject();
        ArrayList<String> searchFields = new ArrayList<>();
        searchFields.add("title");
        searchFields.add("developer");
        searchFields.add("genres");
        searchFields.add("platform");
        searchFields.add("description");
        searchFields.add("status");

        try {
            multi_match.put("query", search);
            multi_match.put("type", "cross_fields");
            multi_match.put("fields", new JSONArray(searchFields));
            multi_match.put("operator", "and");
            query.put("multi_match", multi_match);
            rv.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public static JSONObject borrowerSchema(String gameID) {
        JSONObject borrowerJSON = new JSONObject();
        JSONObject fieldJSON = new JSONObject();
        JSONObject matchJSON = new JSONObject();
        try {
            fieldJSON.put("borrowing_games", gameID);
            matchJSON.put("match", fieldJSON);
            borrowerJSON.put("query", matchJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return borrowerJSON;
    }

    public static JSONObject myAcceptedBidsSchema() {
        JSONObject object = new JSONObject();
        JSONObject query = new JSONObject();
        JSONObject bool = new JSONObject();
        JSONObject must = new JSONObject();
        JSONObject match1 = new JSONObject();
        JSONObject match2 = new JSONObject();

        try {
            match1.put("bids.bidder", Constants.CURRENT_USER.getId());
            match2.put("bids.status", Constants.ACCEPTED);
            must.put("match", match1);
            must.put("match", match2);
            bool.put("must", must);
            query.put("bool", bool);
            object.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject myWatchlistChangesSchema() {
        JSONObject object = new JSONObject();
        JSONObject query1 = new JSONObject();
        JSONObject filtered = new JSONObject();
        JSONObject query2 = new JSONObject();
        JSONObject match = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject ids = new JSONObject();
        ArrayList<String> values = Constants.CURRENT_USER.getWatchlist();

        try {
            match.put("status", Constants.AVAILABLE);
            query2.put("match", match);
            ids.put("type", "games");
            ids.put("values", new JSONArray(values));
            filter.put("ids", ids);
            filtered.put("query", query2);
            filtered.put("filter", filter);
            query1.put("filtered", filtered);
            object.put("query", query1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject myNewBidsSchema() {
        JSONObject object = new JSONObject();
        JSONObject query1 = new JSONObject();
        JSONObject query2 = new JSONObject();
        JSONObject filtered = new JSONObject();
        JSONObject bool = new JSONObject();
        JSONObject must_not = new JSONObject();
        JSONObject missing = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject ids = new JSONObject();
        ArrayList<String> values = Constants.CURRENT_USER.getGames();

        try {
            missing.put("field", "bids");
            must_not.put("missing", missing);
            bool.put("must_not", must_not);
            filter.put("bool", bool);
            ids.put("type", "games");
            ids.put("values", new JSONArray(values));
            query2.put("ids", ids);
            filtered.put("query", query2);
            filtered.put("filter", filter);
            query1.put("filtered", filtered);
            object.put("query", query1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static JSONArray bidsSchema(ArrayList<Bid> bids) {
        JSONArray bidsJSON = new JSONArray();
        for (Bid bid : bids) {
            bidsJSON.put(bidSchema(bid));
        }
        return bidsJSON;
    }

    private static JSONObject bidSchema(Bid bid) {
        JSONObject bidJSON = new JSONObject();
        try {
            bidJSON.put("bidder", bid.getBidder());
            bidJSON.put("price", bid.getPrice());
            bidJSON.put("latitude", bid.getLatitude());
            bidJSON.put("longitude", bid.getLongitude());
            bidJSON.put("status", bid.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bidJSON;
    }

    private static JSONArray reviewsSchema(ArrayList<Review> reviews) {
        JSONArray reviewsJSON = new JSONArray();
        for (Review review : reviews) {
            reviewsJSON.put(reviewSchema(review));
        }
        return reviewsJSON;
    }

    private static JSONObject reviewSchema(Review review) {
        JSONObject reviewJSON = new JSONObject();
        try {
            reviewJSON.put("timestamp", review.getTimestamp());
            reviewJSON.put("reviewBody", review.getReviewBody());
            reviewJSON.put("rating", review.getRating());
            reviewJSON.put("reviewer", review.getReviewer());
            reviewJSON.put("gameID", review.getGameID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewJSON;
    }


}
