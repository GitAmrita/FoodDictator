package amrita.fooddictator.Listeners;

import java.util.List;

import amrita.fooddictator.Objects.Restaurant;

/**
 * Created by amritachowdhury on 8/10/17.
 */

public interface GetYelpRestaurantRecommendationListener {
    void getYelpRestaurantRecommendation(List<Restaurant> recommendedRestaurants);
}
