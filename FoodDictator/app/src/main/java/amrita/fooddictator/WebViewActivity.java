package amrita.fooddictator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by amritachowdhury on 8/11/17.
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private String webUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.restaurant_details);
        webView.getSettings().setJavaScriptEnabled(true);
        readIntent();
        webView.loadUrl(webUrl);
    }

    private void readIntent() {
        if (getIntent().hasExtra("selectedRestaurantName")) {
           webUrl = getIntent().getStringExtra("selectedRestaurantName");
            webUrl = "http://www.google.com";
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "selectedRestaurantName");
        }
    }
}
