package amrita.fooddictator.RecommendationEngine;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import amrita.fooddictator.Objects.Restaurant;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by amritachowdhury on 8/10/17.
 */

public class YelpSearchAPIAsync extends AsyncTask<Void, Void, String> {
    public Context context;
    public RecommendRestaurant recommendObj;
    public String searchUrl; public String accessToken;
    List<Restaurant> recommendedRestaurant;

    public YelpSearchAPIAsync(Context context, String searchUrl, String accessToken) {
        recommendObj = new RecommendRestaurant();
        this.searchUrl = searchUrl;
        this.accessToken = accessToken;
        this.recommendedRestaurant = new ArrayList<>(10);
    }
    private  String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }

    private void restaurantParser(JSONObject json) {
        try {
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
                recommendedRestaurant.add(r);
            }
        } catch (Exception ex) {

        }
    }
    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet();
            URI uri = new URI(searchUrl);
            httpGet.setURI(uri);
            httpGet.setHeader("Authorization", "Bearer " + accessToken);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            InputStream is = entity.getContent();
            String str = convertStreamToString(is);
            return str;
        } catch (Exception e) {
            String h = e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            String g = result;
            JSONObject json = new JSONObject(g);
            restaurantParser(json);
        } catch (Exception e) {
            String h = e.getMessage();
        }
    }
}
