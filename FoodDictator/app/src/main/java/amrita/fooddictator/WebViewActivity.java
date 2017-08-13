package amrita.fooddictator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by amritachowdhury on 8/11/17.
 */
/*
* Functionalities of the screen
* 1. Displays the google page for the selected restaurant from the list of recommended restaurants.
* */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String webUrl;
    private static final String BASE_URL = "https://www.google.com/search?q=";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.restaurant_details);
        webView.getSettings().setJavaScriptEnabled(true);
        readIntent();
        webView.loadUrl(webUrl);
    }

    private void readIntent() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        if (getIntent().hasExtra("selectedRestaurantName")) {
           String restaurantName = getIntent().getStringExtra("selectedRestaurantName");
            String[] splitted = restaurantName.split("\\s+");
            for (String s : splitted) {
                sb.append(s + "+");
            }
            webUrl = sb.toString();
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "selectedRestaurantName");
        }
    }
}
