package ca.ualberta.cmput301w16t18.gamexchange;

import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vassili Minaev on 3/4/2016.
 */
public class Parser {
    private String JSON;

    public Parser(String JSON) {
        this.JSON = JSON;
    }

    public String getStringValue(String key) {
        Pattern r = Pattern.compile("\"" + key + "\"(?: ?: ?)\"([^\"]+)\"");
        Matcher m = r.matcher(JSON);

        if (m.find()) { return m.group(1); }

        return "";
    }

    public ArrayList<String> getArrayValue(String key) {
        Pattern r = Pattern.compile("\"" + key + "\"(?: ?: ?)\\[([^\\]]+)\\]");
        Matcher m = r.matcher(JSON);
        ArrayList<String> rv = new ArrayList<String>();

        if (m.find()) {
            String items = m.group(1);
            for (String item : items.split("\\s*,\\s*")) {
                rv.add(item.substring(1,item.length()-1));
            }
        }

        return rv;
    }
}