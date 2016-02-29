package ca.ualberta.cmput301w16t18.gamexchange;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SearchListActivity extends AppCompatActivity {

    //modified from http://developer.android.com/training/gestures/detector.html
    private GestureDetectorCompat mDetector;
    protected SearchListListViewArrayAdapter adapter;

    public GameList games = new GameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        //create dummy data. TODO: remove this.
        for(int i = 0; i < 10; i++) {
            games.add(new Game());
        }

        //Initialize ListView
        ListView listView = (ListView) findViewById(R.id.searchListActivityListView);

        adapter = new SearchListListViewArrayAdapter(this, games.getGames());
        listView.setAdapter(adapter);

        l
        //Initialize Gesture detector

        mDetector = new GestureDetectorCompat(, new CustomGestureDetector(listView));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Touch Event Registered. " + event.toString());
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void addGame(Game mygame, String userId) {
        //implements US 01.01.01
        //add a game to a user's list
        games.add(mygame);
    }

    public void deleteGame(Game mygame) {
        //implements US 01.05.01
        //deletes a game from a user's list
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

    // modified from http://stackoverflow.com/questions/12713926/showing-a-delete-button-on-swipe-in-a-listview-for-android
    protected class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private ListView listView;
        private int SWIPE_MIN_DISTANCE = 1;
        private int SWIPE_THRESHOLD_VELOITY = 1;

        public CustomGestureDetector(ListView listView) {
            this.listView = listView;
        }

        //Conditions are going to be velocity and distance
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOITY) {
                if(showEditButton(e1)) {
                    return true;
                }
            //}
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        private boolean showEditButton(MotionEvent e1) {
            int pos = listView.pointToPosition((int) e1.getX(), (int) e1.getY());
            return showEditButton(pos);
        }

        private boolean showEditButton(int position) {
             View child = listView.getChildAt(position);
            if(child != null) {
                Button edit = (Button) child.findViewById(R.id.SearchListEditButton);
                if(edit != null) {
                    if(edit.getVisibility() == View.INVISIBLE) {
                        edit.setVisibility(View.VISIBLE);
                    }
                    else {
                        edit.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }
            else {
                System.out.println("\n\n\n\n\n\n\n\n\n\nChild is null.\n\n\n\n\n\n");
            }
            return false;
        }


    }

}
