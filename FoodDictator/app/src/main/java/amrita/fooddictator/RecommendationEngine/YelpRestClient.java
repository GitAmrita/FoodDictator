package amrita.fooddictator.RecommendationEngine;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by amritachowdhury on 8/9/17.
 */
/*
* Functionalities
* 1. REST api client from the library android-async-http:1.4.9
* */

public class YelpRestClient {
    private static final String BASE_URL = "https://api.yelp.com/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, BasicHeader[] header, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String u = getAbsoluteUrl(url);
        client.get(null, u, header, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String u = getAbsoluteUrl(url);
        client.post(u, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
