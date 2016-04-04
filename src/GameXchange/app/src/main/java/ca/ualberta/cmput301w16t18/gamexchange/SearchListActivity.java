package ca.ualberta.cmput301w16t18.gamexchange;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * This activity displays a list of games depending on context
 */
public class SearchListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchListListViewArrayAdapter adapter;
    private SearchListActivity searchListActivity;
    private FloatingActionButton fab;
    private Menu searchMenu;

    private View mProgressView;
    private View mListViewView;

    public GameList games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchListActivity = this;
        games = new GameList();
        handleIntent(getIntent());
        setContentView(R.layout.activity_search_list);

        mListViewView = findViewById(R.id.searchListAllView);
        mProgressView = findViewById(R.id.search_progress);

        //Initialise FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchListActivity.this, GameProfileEditActivity.class);
                intent.putExtra(Constants.GAME_ID, "");
                startActivity(intent);
            }
        });

        //Create Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerListView = (ListView) findViewById(R.id.left_drawer);
        // set adapter for drawer ListView
        drawerListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.navigation_drawer_items_section)));
        //Set onclick listener
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException ex) {
            Log.d("Navigation Drawer", "Got a null pointer from get support action bar. " + ex.getMessage());
        }
        getSupportActionBar().setHomeButtonEnabled(true);

        //mTitle = mDrawerTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Initialize ListView
        ListView listView = (ListView) findViewById(R.id.searchListActivityListView);
        adapter = new SearchListListViewArrayAdapter(this, games.getGames());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchListActivity.this, GameProfileViewActivity.class);
                String gameID = games.getGames().get(position).getId();
                intent.putExtra(Constants.GAME_ID, gameID);
                startActivity(intent);
            }
        });

        //Initialize Gesture detector
        CustomGestureDetector mDetector = new CustomGestureDetector(this, SearchListActivity.this, listView);
        listView.setOnTouchListener(mDetector);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // This is for the Tutorial
        // For reuse statement https://github.com/deano2390/MaterialShowcaseView
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(200); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "ListView");

        sequence.setConfig(config);

        sequence.addSequenceItem(findViewById(R.id.tutorialTextView),
                "This is where you will see all of your Notifications but you dont have any yet!", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.tutorialTextView),
                "Swipe from the left to open the hamburger menu!", "GOT IT");

        sequence.addSequenceItem(findViewById(R.id.tutorialTextView),
                "Now you're ready to navigate around the app!", "GOT IT");

        sequence.start();
        TextView view = (TextView) findViewById(R.id.tutorialTextView);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (Constants.SEARCHLIST_CONTEXT) {
            case "":
                Log.d("Null Pointer", "Intent for SearchListActivity was started without the SEARCH_LIST_ACTIVITY_ACTION added");
                break;
            case Constants.NOTIFICATIONS:
                setTitle("Notifications");
                fab.setVisibility(View.GONE);
                break;
            case Constants.BORROWED_GAMES:
                setTitle("Borrowed Games");
                fab.setVisibility(View.GONE);
                break;
            case Constants.WATCH_LIST:
                setTitle("Watch List");
                fab.setVisibility(View.GONE);
                break;
            case Constants.MY_BIDS:
                setTitle("My Bids");
                fab.setVisibility(View.GONE);
                break;
            default:
                //Default to my Games
                setTitle("My Games");
                fab.setVisibility(View.VISIBLE);
                break;
        }
        showProgress(true);
        ElasticSearcher.receiveGames(this);
    }

    /**
     * Used to create the search bar and use it
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        searchMenu = menu;
        inflater.inflate(R.menu.global, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }


    /**
     * retrieves appropriate data passed through the intent
     * @param intent parent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            ElasticSearcher.receiveGamesBySearchTerm(query, searchListActivity);
            setTitle("Results for \"" + query + "\"");

            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) searchMenu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconified(true);
            showProgress(false);
        }
    }

    @Override
    public void onBackPressed() {
        //Modified from http://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
        new AlertDialog.Builder(this).setMessage(R.string.alert_dialog_message)
                .setPositiveButton(R.string.alert_dialog_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.CURRENT_USER = new User();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
        .show();
        }


    /**
     * Updates the primary ArrayList with a new list of games.
     * @param gameList list of games.
     */
    public void setDisplayedList (GameList gameList) {
        games.clear();
        games.addAll(gameList);
        adapter.notifyDataSetChanged();
        showProgress(false);
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
        // Fixed as per lint.
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * adds a game to the elastic search database along with the owner.
     * @param mygame game to be added
     */
    public void addGame(Game mygame) {
        //implements US 01.01.01
        //add a game to a user's list
        games.add(mygame);
    }

    /**
     * deletes a game from Elastic search by game ID
     * @param id game id to be deleted
     */
    public void deleteGame(String id) {
        games.removeGame(id);
        adapter.notifyDataSetChanged();
    }

    /**
     * Deletes a game from Elastic search by position
     * @param position position in the gameslist to be deleted
     */
    public void deleteGameByPosition(int position) {
        //implements US 01.05.01
        //deletes a game from a user's list
        Game mygame = games.getGames().get(position);
        if (Constants.SEARCHLIST_CONTEXT.equals(Constants.WATCH_LIST)){
            ElasticSearcher.removeGameFromList(mygame.getId());
            deleteGame(mygame.getId());
        }else if(Constants.SEARCHLIST_CONTEXT.equals(Constants.MY_GAMES)){
            ElasticSearcher.deleteGame(mygame.getId(), this);
        }else if(Constants.SEARCHLIST_CONTEXT.equals(Constants.BORROWED_GAMES)){
            CharSequence text = "Cannot delete";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(searchListActivity, text, duration);
            toast.show();
        }
    }

    /**
     * Listener for navigation drawer item clicks
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //Use position to navigate to the right dataset.
            //Added Switch cases to control where the buttons will go
            Intent intent;
            switch (position){
                case 0:
                    Constants.SEARCHLIST_CONTEXT = Constants.MY_GAMES;
                    setTitle("My Games");
                    fab.setVisibility(View.VISIBLE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 1:
                    Constants.SEARCHLIST_CONTEXT = Constants.BORROWED_GAMES;
                    setTitle("Borrowed Games");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    Constants.SEARCHLIST_CONTEXT = Constants.WATCH_LIST;
                    setTitle("Watch List");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.receiveGames(searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:
                    Constants.SEARCHLIST_CONTEXT = Constants.MY_BIDS;
                    setTitle("My Bids");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.receiveMyBids(searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 4:
                    Constants.SEARCHLIST_CONTEXT = Constants.NOTIFICATIONS;
                    setTitle("Notifications");
                    fab.setVisibility(View.GONE);
                    showProgress(true);
                    ElasticSearcher.getNotifications(searchListActivity);
                    mDrawerLayout.closeDrawers();
                    break;
                case 5:
                    intent = new Intent(SearchListActivity.this, UserProfileViewActivity.class);
                    intent.putExtra(Constants.USER_ID, Constants.CURRENT_USER.getId());
                    startActivity(intent);
                    break;
                case 6:
                    Constants.CURRENT_USER = new User();
                    finish();
                    break;
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListViewView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListViewView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
