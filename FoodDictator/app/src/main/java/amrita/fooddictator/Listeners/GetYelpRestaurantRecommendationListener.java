package amrita.fooddictator.Listeners;

import java.util.List;

import amrita.fooddictator.Objects.Restaurant;

/**
 * Created by amritachowdhury on 8/10/17.
 */
/* Functionalities:
1. This class contains the listener when the list of restaurants is returned by the yelp search api.
* */

public interface GetYelpRestaurantRecommendationListener {
    void getYelpRestaurantRecommendation(List<Restaurant> recommendedRestaurants);
}
