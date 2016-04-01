package ca.ualberta.cmput301w16t18.gamexchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by cawthorn on 2/28/16.
 */
public class BidListViewArrayAdapter extends ArrayAdapter<Bid> {
    private final Context context;
    private ArrayList<Bid> bids;
    private Game game;

    /**
     * Constructor for an android arrayadapter
     * @param context context to create the arrayadapter
     * @param game game to be adaptered.
     */
    public BidListViewArrayAdapter(Context context, Game game) {
        super(context,-1,game.getBids());
        this.context = context;
        this.game = game;
        this.bids = game.getBids();
        this.add(new Bid());
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        if(position == 0) return 0;
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getItemViewType(position) == 0) {
            convertView = inflater.inflate(R.layout.game_profile_listview_item, parent, false);
            TextView game_view_title = (TextView) convertView.findViewById(R.id.game_view_title);
            TextView game_view_developer = (TextView) convertView.findViewById(R.id.game_view_developer);
            TextView game_view_platform = (TextView) convertView.findViewById(R.id.game_view_platform);
            TextView game_view_genres = (TextView) convertView.findViewById(R.id.game_view_genres);
            TextView game_view_description = (TextView) convertView.findViewById(R.id.game_view_description);
            ImageView game_view_image = (ImageView) convertView.findViewById(R.id.game_view_image);

            game_view_title.setText(game.getTitle());
            game_view_developer.setText(game.getDeveloper());
            game_view_platform.setText(game.getPlatform());
            game_view_genres.setText(TextUtils.join(", ", game.getGenres()));
            game_view_description.setText(game.getDescription());

            if (!game.getPicture().equals("")) {
                //Decode base64 string to a bitmap.
                byte[] decodedBytes = Base64.decode(game.getPicture(), 0);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                game_view_image.setImageBitmap(imageBitmap);
            }
        } else {

            convertView = inflater.inflate(R.layout.bid_listview_item, parent, false);

            //Populate data in the view
            Bid bid = bids.get(position - 1);

            TextView textview = (TextView) convertView.findViewById(R.id.BidListItemItemAmountTextView);
            String text = String.valueOf(bid.getPrice());
            try {
                textview.setText("$" + text + " / day");
            } catch (NullPointerException ex) {
                System.out.println("text was null.");
                textview.setText("Text was null");
            }
        }
        return convertView;
    }
}
