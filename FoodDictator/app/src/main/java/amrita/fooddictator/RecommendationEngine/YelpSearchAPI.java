package amrita.fooddictator.RecommendationEngine;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import amrita.fooddictator.Listeners.GetYelpRestaurantRecommendationListener;
import amrita.fooddictator.Objects.Restaurant;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;


/**
 * Created by amritachowdhury on 8/9/17.
 */
/*
* Functionalities
* 1. Gets the list of restaurants based on query params (location, recommended category) using yelp's V3 REST api
* */

public class YelpSearchAPI {
    public  List<Restaurant> recommendedRestaurants = new ArrayList<>();

    private static final String searchUrl = "v3/businesses/search";

    public void getRestaurantsFromApi(String zipCode, int noOfResults, String sortBy,
                                      boolean isOpenNow, String searchTerm, String categories,
                                      String accessToken,
                                      final GetYelpRestaurantRecommendationListener listener) throws JSONException {
        RequestParams params = setRequestParams(zipCode, noOfResults, sortBy, isOpenNow,
                searchTerm, categories);
        BasicHeader[] header = new BasicHeader[] {new BasicHeader("Authorization", "Bearer " + accessToken)};
        YelpRestClient.get(searchUrl, header, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    recommendedRestaurants = restaurantParser(response);
                    listener.getYelpRestaurantRecommendation(recommendedRestaurants);
                } catch (Exception e) {
                    Log.e("bapi", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.e("bapi", "oo");
            }
        });
    }

    private RequestParams setRequestParams(String zipCode, int noOfResults, String sortBy,
                                                boolean isOpenNow, String searchTerm, String categories) {
        RequestParams params = new RequestParams();
        params.put("location", zipCode);
        params.put("limit", noOfResults);
        params.put("sort_by", sortBy);
        params.put("open_now", isOpenNow);
        params.put("term", searchTerm);
        if (categories != null && !categories.isEmpty()) {
            params.put("categories", categories);
        }
        return params;
    }

    private List<Restaurant> restaurantParser(JSONObject json) {
        try {
            List<Restaurant> recommendedRestaurants = new ArrayList<>(10);
            JSONArray restaurants = json.getJSONArray("businesses");
            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject restaurant = restaurants.getJSONObject(i);
                String name = restaurant.getString("name");
                JSONObject location = restaurant.getJSONObject("location");
                String address = String.format("%s, %s, %s", location.getString("address1"),
                        location.getString("city"), location.getString("state"));
                String phone = restaurant.getString("display_phone");
                JSONArray categoriesArray = restaurant.getJSONArray("categories");
                String category = categoriesArray.getJSONObject(0).getString("alias");
                int reviewCount = restaurant.getInt("review_count");
                float rating = (float)restaurant.getDouble("rating");
                Restaurant r = new Restaurant(name, address, phone, category, reviewCount, rating);
                recommendedRestaurants.add(r);
            }
            return recommendedRestaurants;
        } catch (Exception ex) {
            return null;
        }
    }
}
