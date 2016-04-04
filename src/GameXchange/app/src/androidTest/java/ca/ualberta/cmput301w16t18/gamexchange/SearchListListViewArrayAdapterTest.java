package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-03-14.
 */
public class SearchListListViewArrayAdapterTest extends AndroidTestCase {

    public void testGetView() throws Exception {
        ArrayList<Game> gamelist = new ArrayList<Game>();
        ArrayList<String> genres = new ArrayList<String>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);
        gamelist.add(game);
        SearchListListViewArrayAdapter adapter = new SearchListListViewArrayAdapter(getContext(), gamelist);

        View view = adapter.getView(0,null,null);
        assertEquals("Title not equal","Title",((TextView) view.findViewById(R.id.SearchListItemTitleTextView)).getText().toString());
        assertEquals("Platform not equal", "platform",((TextView) view.findViewById(R.id.SearchListItemPlatformTextView)).getText().toString());
        assertEquals("Status not equal","Available",((TextView) view.findViewById(R.id.SearchListItemStatusTextView)).getText().toString());
    }

    public void testGetID() throws Exception {
        ArrayList<Game> gamelist = new ArrayList<Game>();
        ArrayList<String> genres = new ArrayList<String>();
        ArrayList<Bid> bids = new ArrayList<>();
        genres.add("genres");
        Game game = new Game("Game-ID","Available","Title","developer","platform",genres,"description","Picture",bids);
        gamelist.add(game);
        SearchListListViewArrayAdapter adapter = new SearchListListViewArrayAdapter(getContext(), gamelist);

        assertEquals("Game-ID does not match", adapter.getID(0), "Game-ID");
    }
}