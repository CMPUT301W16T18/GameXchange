package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

// modified from http://developer.android.com/training/gestures/detector.html
// and http://stackoverflow.com/questions/12713926/showing-a-delete-button-on-swipe-in-a-listview-for-android
class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener
        implements ListView.OnTouchListener {
    private ListView listView;
    private GestureDetector gestureDetector;
    private SearchListActivity searchListActivity;

    public CustomGestureDetector(SearchListActivity searchListActivity, Context context, ListView listView) {
        GestureDetector detector = new GestureDetector(context, this);

        this.listView = listView;
        this.gestureDetector = detector;
        this.searchListActivity = searchListActivity;
    }

    //Conditions are going to be velocity and distance
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        final int position = listView.pointToPosition(
                Math.round(e1.getX()),
                Math.round(e1.getY())
        );

        if(e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
            //System.out.println("Swiped Right");
            if(hideDeleteButton(position)) {
                return true;
            }
        }
        if(e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
            //System.out.println("Swiped Left");
            if(showDeleteButton(position)) {
                return true;
            }
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    private boolean hideDeleteButton(int position) {
        View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
        if(child != null) {
            Button delete = (Button) child.findViewById(R.id.SearchListDeleteButton);
            if(delete != null) {
                if(delete.getVisibility() == View.VISIBLE) {
                    delete.setOnClickListener(null);

                    Animation buttonSwipe = AnimationUtils.loadAnimation(child.getContext(),
                            R.anim.search_delete_button_hide_animation);
                    delete.setAnimation(buttonSwipe);
                    delete.animate();
                    delete.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        }
        else {
            System.out.println("Child is null. Position: " + position);
        }
        return false;
    }

    private boolean showDeleteButton(final int position) {
        View child = listView.getChildAt(position - listView.getFirstVisiblePosition());
        if(child != null) {
            Button delete = (Button) child.findViewById(R.id.SearchListDeleteButton);
            if(delete != null) {
                if(delete.getVisibility() == View.INVISIBLE) {
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchListActivity.deleteGameByPosition(position);
                        }
                    });
                    Animation buttonSwipe = AnimationUtils.loadAnimation(child.getContext(),
                            R.anim.search_delete_button_show_animation);
                    delete.setAnimation(buttonSwipe);
                    delete.setVisibility(View.VISIBLE);
                    delete.animate();
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
