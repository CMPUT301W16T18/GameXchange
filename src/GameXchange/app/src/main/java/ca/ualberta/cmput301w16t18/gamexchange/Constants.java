package ca.ualberta.cmput301w16t18.gamexchange;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
class Constants {
    public static boolean testing = false;
    public static final boolean DEBUG = false;

    public static final String serverURL = "http://elastic.vassi.li:9200/"; // TODO: Change this later
    public static final String index = "gamexchange";
    public static final String testingIndex = "gamexchangetest";

    public static String getPrefix() {
        if (testing) { return serverURL + testingIndex + "/"; }
        return serverURL + index + "/";
    }

    public static final String GAME_ID = "GAME_ID";
    public static final String USER_ID = "USER_ID";
    public static User CURRENT_USER = new User();
    public static String SEARCHLIST_CONTEXT = "";
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final int SWIPE_MIN_DISTANCE = 300;
    public static final int SWIPE_THRESHOLD_VELOCITY = 150;

    public static final String ALL_GAMES = "ALL_GAMES";
    public static final String MY_GAMES = "MY_GAMES";
    public static final String BORROWED_GAMES = "BORROWED_GAMES";
    public static final String WATCH_LIST = "WATCH_LIST";

    public static final String ACCEPTED = "ACCEPTED";
    public static final String REJECTED = "REJECTED";
    public static final String PENDING = "PENDING";

    public static final String SEARCH_LIST_ACTIVITY_ACTION = "SEARCH_LIST_ACTIVITY_ACTION";
    public static final String FILENAME = "cache.sav";
    public static int iterator = 0;

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
