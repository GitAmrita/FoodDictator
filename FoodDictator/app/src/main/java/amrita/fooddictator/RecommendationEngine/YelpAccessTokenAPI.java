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

import static amrita.fooddictator.Config.YELP_ACCESS_TOKEN_EXPIRY_THRESHOLD;

/**
 * Created by amritachowdhury on 8/9/17.
 */

/*
* Functionalities
* 1. Gets the access token from yelp, to make further calls to yelp V3 REST api
* 2. Validates the saved token since yelp token typically expire by certain time.
* */

public class YelpAccessTokenAPI {
    public static String yelpAccessToken;
    public static int yelpAccessTokenExpiry;
    private static final String accessTokenUrl = "oauth2/token";

    public final String TAG = this.getClass().getName();

    public void requestAccessTokenFromApi(final GetYelpAccessTokenListener listener) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("client_id", Config.YELP_CLIENT_ID);
        params.put("client_secret", Config.YELP_CLIENT_SECRET);
        YelpRestClient.post(accessTokenUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    yelpAccessToken = response.getString("access_token");
                    yelpAccessTokenExpiry = response.getInt("expires_in");
                    listener.getYelpAccessToken(yelpAccessToken, yelpAccessTokenExpiry);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
            }
        });
    }

    public boolean isAccessTokenValid(String token, int expiresIn, long createdAt) {
        long currentTimeInSeconds = System.currentTimeMillis()/1000;
        if (token == null || token.isEmpty() || expiresIn == 0 ||
                currentTimeInSeconds > createdAt + expiresIn - YELP_ACCESS_TOKEN_EXPIRY_THRESHOLD) {
            return false;
        } // to be on the safe side, get new token 2 days (YELP_ACCESS_TOKEN_EXPIRY_THRESHOLD) before expiry.
        return true;
    }
}
