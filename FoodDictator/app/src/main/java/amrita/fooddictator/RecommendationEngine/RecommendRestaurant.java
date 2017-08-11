package amrita.fooddictator.RecommendationEngine;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import amrita.fooddictator.Config;
import amrita.fooddictator.Listeners.GetYelpAccessTokenListener;
import amrita.fooddictator.Listeners.GetYelpRestaurantRecommendationListener;
import amrita.fooddictator.Objects.Player;
import amrita.fooddictator.Objects.Restaurant;
import amrita.fooddictator.RecommendationActivity;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by amritachowdhury on 8/9/17.
 */

public class RecommendRestaurant {
    private RecommendationActivity activity;
    public YelpAccessTokenAPI tokenAPI;
    public YelpSearchAPI searchAPI;

    public RecommendRestaurant() {};

    public RecommendRestaurant(RecommendationActivity activity) {
        try {
            this.activity = activity;
            this.tokenAPI = new YelpAccessTokenAPI();
            this.searchAPI = new YelpSearchAPI();
        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }
    }

    public void runRecommendation(Player foodDictator, String location) {
        requestAccessToken(foodDictator, location);
    }

    public void getRecommendation (Player foodDictator, final String accessToken, String location) {
        try {
            final Map<String, Integer> recommendationMap = foodDictator.getRecommendations();
            String categories = recommendationMap != null && recommendationMap.size() > 0 ?
                    createRecommendationCategory(foodDictator) : "";
           // String categories = "";
            searchAPI.getRestaurantsFromApi(location, Config.NUMBER_OF_RESTAURANT_RESULTS,
                    Config.DEFAULT_SORT_BY_RESTAURANTS, true, Config.DEFAULT_SEARCH_TERM,
                    categories, accessToken, new GetYelpRestaurantRecommendationListener() {
                        @Override
                        public void getYelpRestaurantRecommendation(List<Restaurant> recommendedRestaurants) {
                            Log.e("bapi", String.valueOf(recommendedRestaurants.size()));
                            activity.displayRecommendedRestaurants(recommendedRestaurants);

                        }
                    });
        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }

    }

    private String createRecommendationCategory(Player foodDictator) {
        LinkedHashMap<String, Integer> m = foodDictator.sortRecommendationsBasedOnPopularity();
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            sb.append(entry.getKey() + ",");
            count++;
            if (count == Config.NO_OF_CATEGORIES_FOR_RESTAURANT_RECOMMENDATION) {
                break;
            }
        }
        sb.setLength(sb.length() - 1); //remove the trailing comma
        return sb.toString();
    }

    public void requestAccessToken(final Player foodDictator, final String location) {
        try {
            final SharedPreferences myPrefs = activity.getSharedPreferences("yelpAccessKey", MODE_PRIVATE );
            String yelpAccessToken = myPrefs.getString("token", null);
            Long yelpAccessTokenExpiry = myPrefs.getLong("expires",0);
            if (!this.tokenAPI.isAccessTokenValid(yelpAccessToken, yelpAccessTokenExpiry)) {
                this.tokenAPI.requestAccessTokenFromApi(new GetYelpAccessTokenListener() {
                    @Override
                    public void getYelpAccessToken(String token, Long expires) {
                        SharedPreferences.Editor e = myPrefs.edit();
                        e.putString("token", token);
                        e.putLong("expires", expires);
                        e.commit();
                        getRecommendation(foodDictator, token, location);
                    }
                });
            } else {
                getRecommendation(foodDictator, yelpAccessToken, location);
            }

        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }
    }
}
