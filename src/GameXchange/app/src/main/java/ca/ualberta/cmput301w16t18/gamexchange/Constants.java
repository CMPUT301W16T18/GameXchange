package ca.ualberta.cmput301w16t18.gamexchange;

/**
 * Created by Vassili Minaev on 2/29/2016.
 */
public class Constants {
    public static String serverURL = "http://elastic.vassi.li:9200/"; // TODO: Change this later
    public static String index = "gamexchange";

    public static String getPrefix() { return serverURL + index + "/"; }

    public static String GAME_ID = "GAME_ID";
    public static String USER_ID = "USER_ID";
    public static String CURRENT_USER = "";
    public static String SEARCHLIST_CONTEXT = "";
    public static int REQUEST_IMAGE_CAPTURE = 1;

    public static int SWIPE_MIN_DISTANCE = 300;
    public static int SWIPE_THRESHOLD_VELOCITY = 150;

    public static Boolean DEBUG = false;

    public static String ALL_GAMES = "ALL_GAMES";
    public static String MY_GAMES = "MY_GAMES";
    public static String BORROWED_GAMES = "BORROWED_GAMES";
    public static String WATCH_LIST = "WATCH_LIST";

    public static String SEARCH_LIST_ACTIVITY_ACTION = "SEARCH_LIST_ACTIVITY_ACTION";

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
