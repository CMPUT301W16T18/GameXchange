package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cawthorn on 2/28/16.
 */
public class ReviewListViewArrayAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private final ArrayList<Game> gamelist;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param gamelist arrayList to be adaptered.
     */
    public ReviewListViewArrayAdapter(Context context, ArrayList<Game> gamelist) {
        super(context,-1,gamelist);
        this.context = context;
        this.gamelist = gamelist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchlist_listview_item, parent, false);
        } else {
            Button button = (Button) convertView.findViewById(R.id.SearchListDeleteButton);
            if(button.getVisibility() == View.VISIBLE) button.setVisibility(View.INVISIBLE);
        }


        //Populate data in the view
        TextView textview = (TextView) convertView.findViewById(R.id.SearchListItemTitleTextView);
        textview.setText(gamelist.get(position).getTitle());

        textview = (TextView) convertView.findViewById(R.id.SearchListItemPlatformTextView);
        textview.setText(gamelist.get(position).getPlatform());

        textview = (TextView) convertView.findViewById(R.id.SearchListItemStatusTextView);
        textview.setText(gamelist.get(position).getStatus());

        return convertView;
    }

    /**
     * returns the game id for the item at the specified position.
     * @param position position to return game id for.
     * @return the gameid
     */
    public String getID(int position) {
        return gamelist.get(position).getId();
    }
}
