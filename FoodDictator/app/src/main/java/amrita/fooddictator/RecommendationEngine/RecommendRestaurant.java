package amrita.fooddictator.RecommendationEngine;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
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
/*
* Functionalities
* 1. Default Recommendation:
* If the food dictator has no restaurant category (medeterranean, indian etc) that he earlier selected,
* give a default list of restaurants based on the location specified. Return NUMBER_OF_DEFAULT_RESTAURANT_RESULTS restaurants
*
* 2. Custom Recommendation:
* If the food dictator has some restaurant category that he earlier selected, then return list of
* restaurants based on his top 3 cuisines (frequency of visits). Return NUMBER_OF_CUSTOM_RESTAURANT_RESULTS restaurants. Also
* return NUMBER_OF_DEFAULT_RESTAURANT_RESULTS - NUMBER_OF_CUSTOM_RESTAURANT_RESULTS default recommendations. The default
* recommendation is added so that he gets some restaurants recommendations that are not of the top 3
* cuisines he frequently visits.
* */


public class RecommendRestaurant {
    private RecommendationActivity activity;
    public YelpAccessTokenAPI tokenAPI;
    public YelpSearchAPI searchAPI;
    List<Restaurant> recommendedRestaurants;

    public RecommendRestaurant(RecommendationActivity activity) {
        try {
            this.activity = activity;
            this.tokenAPI = new YelpAccessTokenAPI();
            this.searchAPI = new YelpSearchAPI();
            this.recommendedRestaurants = new ArrayList<>();
        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }
    }

    public void runRecommendation(Player foodDictator, String location) {
        activity.spinner.setVisibility(View.VISIBLE);
        requestAccessToken(foodDictator, location);
    }

    public void getRecommendation (Player foodDictator, final String accessToken, String location) {
        try {
            final Map<String, Integer> recommendationMap = foodDictator.getRecommendations();
            String categories = recommendationMap != null && recommendationMap.size() > 0 ?
                    createRecommendationCategory(foodDictator) : "";
            if (categories.isEmpty()) {
                getDefaultRecommendation(accessToken, location,
                        Config.NUMBER_OF_DEFAULT_RESTAURANT_RESULTS);
            } else {
                int noOfDefaultResults = Config.NUMBER_OF_DEFAULT_RESTAURANT_RESULTS -
                        Config.NUMBER_OF_CUSTOM_RESTAURANT_RESULTS;
                getCustomRecommendation(categories, accessToken, location,
                        Config.NUMBER_OF_CUSTOM_RESTAURANT_RESULTS);
                getDefaultRecommendation(accessToken, location, noOfDefaultResults);
            }
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

    private void getDefaultRecommendation(final String accessToken, String location,
                                          int noOfResults) {
        try {
            searchAPI.getRestaurantsFromApi(location, noOfResults,
                    Config.DEFAULT_SORT_BY_RESTAURANTS, true, Config.DEFAULT_SEARCH_TERM,
                    "", accessToken, new GetYelpRestaurantRecommendationListener() {
                        @Override
                        public void getYelpRestaurantRecommendation(List<Restaurant> restaurants) {
                            recommendedRestaurants.addAll(restaurants);
                            activity.displayRecommendedRestaurants(recommendedRestaurants);

                        }
                    });
        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }
    }

    private void getCustomRecommendation(String categories, final String accessToken,
                                         String location, int noOfResults) {
        try {
            searchAPI.getRestaurantsFromApi(location, noOfResults,
                    Config.DEFAULT_SORT_BY_RESTAURANTS, true, Config.DEFAULT_SEARCH_TERM,
                    categories, accessToken, new GetYelpRestaurantRecommendationListener() {
                        @Override
                        public void getYelpRestaurantRecommendation(List<Restaurant> restaurants) {
                            recommendedRestaurants.addAll(restaurants);
                        }
                    });
        } catch (Exception e) {
            Log.e("bapi", e.getMessage());
        }
    }

    public void requestAccessToken(final Player foodDictator, final String location) {
        try {
            final SharedPreferences myPrefs = activity.getSharedPreferences("yelpAccessKey", MODE_PRIVATE );
            String yelpAccessToken = myPrefs.getString("token", null);
            int yelpAccessTokenExpiry = myPrefs.getInt("expires_in",0);
            long createdAt = myPrefs.getLong("created_at",0);
            if (!this.tokenAPI.isAccessTokenValid(yelpAccessToken, yelpAccessTokenExpiry, createdAt)) {
                this.tokenAPI.requestAccessTokenFromApi(new GetYelpAccessTokenListener() {
                    @Override
                    public void getYelpAccessToken(String token, int expiresIn) {
                        long currentTimeInSeconds = System.currentTimeMillis()/1000;
                        SharedPreferences.Editor e = myPrefs.edit();
                        e.putString("token", token);
                        e.putInt("expires_in", expiresIn);
                        e.putLong("created_at", currentTimeInSeconds);
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
