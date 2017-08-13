package amrita.fooddictator.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amrita.fooddictator.Objects.Restaurant;
import amrita.fooddictator.R;

/**
 * Created by amritachowdhury on 8/11/17.
 */

/* Functionalities:
1. Adapter for list of recommended restaurants.
* */

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    private final Context context;
    private final List<Restaurant> recommendedRestaurants;

    public RestaurantAdapter(Context context, List<Restaurant> values) {
        super(context, R.layout.list_restaurants, values);
        this.context = context;
        this.recommendedRestaurants = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_restaurants, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.restaurant_name);
        nameView.setText(recommendedRestaurants.get(position).getName());
        nameView.setPaintFlags(nameView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView addressView = (TextView) rowView.findViewById(R.id.restaurant_address);
        TextView categoryView = (TextView) rowView.findViewById(R.id.restaurant_category);
        TextView yelpRatingView = (TextView) rowView.findViewById(R.id.yelp_rating);
        TextView phoneNumberView = (TextView) rowView.findViewById(R.id.restaurant_phone);
        addressView.setText(String.valueOf(recommendedRestaurants.get(position).getAddress()));
        categoryView.setText(recommendedRestaurants.get(position).getCategory());
        String yelpRating = String.format("%s %.1f", yelpRatingView.getText().toString(),
                recommendedRestaurants.get(position).getRating());
        yelpRatingView.setText(yelpRating);
        phoneNumberView.setText(recommendedRestaurants.get(position).getPhoneNumber());
        return rowView;
    }
}
