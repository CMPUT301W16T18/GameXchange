package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class SearchListActivity extends AppCompatActivity {

    //modified from http://developer.android.com/training/gestures/detector.html
    private CustomGestureDetector mDetector;
    protected SearchListListViewArrayAdapter adapter;
    protected ListView listView;

    public GameList games = new GameList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        //create dummy data. TODO: remove this.
        for(int i = 0; i < 1000; i++) {
            games.add(new Game());
        }

        //Initialize ListView
        listView = (ListView) findViewById(R.id.searchListActivityListView);
        adapter = new SearchListListViewArrayAdapter(this, games.getGames());
        listView.setAdapter(adapter);


        //Initialize Gesture detector

        mDetector = new CustomGestureDetector(SearchListActivity.this, listView);
        listView.setOnTouchListener(mDetector);
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
    protected class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener
            implements ListView.OnTouchListener {
        private Context context;
        private ListView listView;
        private GestureDetector gestureDetector;

        private int SWIPE_MIN_DISTANCE = 100;
        private int SWIPE_THRESHOLD_VELOCITY = 10;

        public CustomGestureDetector() {
            super();
        }

        public CustomGestureDetector(Context context, ListView listView) {
            GestureDetector detector = new GestureDetector(context, this);

            this.context = context;
            this.gestureDetector = detector;
            this.listView = listView;
        }

        public CustomGestureDetector(Context context, GestureDetector detector) {
            if(detector == null) {
                detector = new GestureDetector(context, this);
            }

            this.context = context;
            this.gestureDetector = detector;
        }

        //Conditions are going to be velocity and distance
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            final int position = listView.pointToPosition(
                    Math.round(e1.getX()),
                    Math.round(e1.getY())
            );

            if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("Swiped Right");
                if(hideEditButton(position)) {
                    return true;
                }
            }
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("Swiped Left");
                if(showEditButton(position)) {
                    return true;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        private boolean hideEditButton(int position) {
            View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
            if(child != null) {
                Button edit = (Button) child.findViewById(R.id.SearchListEditButton);
                if(edit != null) {
                    if(edit.getVisibility() == View.VISIBLE) {
                        edit.setOnClickListener(null);
                        edit.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }
            else {
                System.out.println("Child is null. Position: " + position);
            }
            return false;
        }

        private boolean showEditButton(final int position) {
            View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
            if(child != null) {
                Button edit = (Button) child.findViewById(R.id.SearchListEditButton);
                if(edit != null) {
                    if(edit.getVisibility() == View.INVISIBLE) {
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent  intent = new Intent(SearchListActivity.this,GameProfileEditActivity.class);
                                String gameID = games.getGames().get(position).getId();
                                intent.putExtra("id", gameID);
                                startActivity(intent);
                            }
                        });
                        edit.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
            else {
                System.out.println("Child is null. Position: " + position);
            }
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public GestureDetector getDetector() {
            return gestureDetector;
        }
    }

}
