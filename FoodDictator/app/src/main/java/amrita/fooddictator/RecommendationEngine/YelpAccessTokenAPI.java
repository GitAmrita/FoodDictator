package amrita.fooddictator.RecommendationEngine;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import amrita.fooddictator.Config;
import amrita.fooddictator.Listeners.GetYelpAccessTokenListener;
import cz.msebera.android.httpclient.Header;

/**
 * Created by amritachowdhury on 8/9/17.
 */

public class YelpAccessTokenAPI {
    public static String yelpAccessToken;
    public static long yelpAccessTokenExpiry = 0;
    private static final String accessTokenUrl = "oauth2/token";


    public void requestAccessTokenFromApi(final GetYelpAccessTokenListener listener) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("client_id", Config.YELP_CLIENT_ID);
        params.put("client_secret", Config.YELP_CLIENT_SECRET);
        YelpRestClient.post(accessTokenUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    yelpAccessToken = response.getString("access_token");
                    listener.getYelpAccessToken(yelpAccessToken, yelpAccessTokenExpiry);
                } catch (Exception e) {
                    Log.e("bapi", e.getMessage());
                }
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                try {
                    JSONObject firstEvent =(JSONObject) timeline.get(0);
                    String accessToken = firstEvent.getString("access_token");
                    Log.e("bapi", accessToken);
                } catch (Exception e) {
                    Log.e("bapi", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
                Log.e("bapi", responseString);
            }
        });
    }

    public boolean isAccessTokenValid(String token, Long expires) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return true;
    }
}
