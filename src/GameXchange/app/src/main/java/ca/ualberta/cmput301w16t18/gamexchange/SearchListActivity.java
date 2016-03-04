package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle, mDrawerTitle;
    private CustomGestureDetector mDetector;
    protected SearchListListViewArrayAdapter adapter;
    protected ListView listView;


    public GameList games = new GameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Search activity");

        setContentView(R.layout.activity_search_list);

        // Customize navigation drawer with this tutorial http://www.tutecentral.com/android-custom-navigation-drawer/
        //Create Navigation Drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        // set adapter for drawer ListView
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.navigation_drawer_items_section)));
        //Set onclick listener
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mTitle = mDrawerTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("GameXchange");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //create dummy data. TODO: remove this, add actual data.
        for(int i = 0; i < 1000; i++) {
            games.add(new Game((Integer.toString(i)), "Available", "Blockbuster Game " + i, "developer", "platform", new ArrayList<String>(), "description", "owner"));
        }

        //Initialize ListView
        listView = (ListView) findViewById(R.id.searchListActivityListView);
        adapter = new SearchListListViewArrayAdapter(this, games.getGames());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent = new Intent(SearchListActivity.this,GameProfileViewActivity.class);
                String gameID = games.getGames().get(position).getId();
                intent.putExtra(Constants.GAME_ID, gameID);
                startActivity(intent);
            }
        });


        //Initialize Gesture detector

        mDetector = new CustomGestureDetector(this, SearchListActivity.this, listView);
        listView.setOnTouchListener(mDetector);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(drawerListView);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addGame(Game mygame, String userId) {
        //implements US 01.01.01
        //add a game to a user's list
        games.add(mygame);
    }

    public void deleteGame(Game mygame) {
        //implements US 01.05.01
        //deletes a game from a user's list
        games.removeGame(mygame);
    }

    public void deleteGameByPosition(int position) {
        deleteGame(games.getGames().get(position));
        adapter.notifyDataSetChanged();
    }

    public void loadOwnedGames(String userId) {
        //implements US 01.02.01
        //pull list of games for specified user and display it
    }

    public void loadBorrowingGames(String userId) {
        //implements US 06.01.01
        //pull list of games that I am borrowing and display it
    }

    public void loadBorrowedGames(String userId) {
        //implements US 06.02.01
        //pull list of games that are borrowed from me and display it
    }

    public void searchGames(String searchString) {
        //implements US 04.01.01 and US 04.02.01
        //search for all available games given searchString
    }

    public void addToWatchlist(Game mygame) {
        //implements US 04.03.01
        //add a game to my watchlist
    }

    public void editButtonClicked() {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //Use position to navigate to the right dataset.
            //Added Switch cases to control where the buttons will go
            Intent intent;
            switch (position){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    intent = new Intent(SearchListActivity.this, UserProfileViewActivity.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(SearchListActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }
}
