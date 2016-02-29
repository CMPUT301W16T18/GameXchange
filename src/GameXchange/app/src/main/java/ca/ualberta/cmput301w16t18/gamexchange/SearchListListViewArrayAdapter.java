package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by cawthorn on 2/28/16.
 */
public class SearchListListViewArrayAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private final ArrayList<Game> gamelist;

    public SearchListListViewArrayAdapter(Context context, ArrayList<Game> gamelist) {
        super(context,-1,gamelist);
        this.context = context;
        this.gamelist = gamelist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchlist_listview_item, parent, false);
        }

        //Populate data in the view


        return convertView;
    }
}
