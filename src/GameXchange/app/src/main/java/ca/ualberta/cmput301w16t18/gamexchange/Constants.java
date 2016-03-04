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

    public static int SWIPE_MIN_DISTANCE = 300;
    public static int SWIPE_THRESHOLD_VELOCITY = 150;

    public static Boolean DEBUG = true;
}
