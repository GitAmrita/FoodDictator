package amrita.fooddictator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amrita.fooddictator.Adapters.RestaurantAdapter;
import amrita.fooddictator.Objects.Helper;
import amrita.fooddictator.Objects.LunchBuddySingleton;
import amrita.fooddictator.Objects.Player;
import amrita.fooddictator.Objects.Restaurant;
import amrita.fooddictator.RecommendationEngine.RecommendRestaurant;

/**
 * Created by amritachowdhury on 8/10/17.
 */
/*
* Functionalities of the screen
* 1. Displays the food dictator by a random pick from the selected players from the list of persistent players.
* 2. Food dictator enters the location (city/ zip) of the neighborhood he wants to go to.
* 3. Click the button to get the list of recommended restaurants.
* 4. If there are no recommended restaurants, a toast is displayed.
* 5. On pressing the back button, all selected players are reset.
* */

public class RecommendationActivity extends AppCompatActivity {

    public ListView allRestaurantsList;
    public TextView foodDictatorName;
    public TextView restaurantRecommendationTitle;
    public EditText restaurantZipCode;
    public Button recommendBtn;
    public RecommendRestaurant recommendRestaurant;
    public Player foodDictator;
    public LunchBuddySingleton lunchBuddySingleton;
    public ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        initComponents();
        drawComponents();
        readIntent();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(Config.RESET_PRESENT_PLAYERS_RESULT_CODE, intent);
        finish();
        super.onBackPressed();
    }

    public void onClickRecommendBtn(View v) {
        if (restaurantZipCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a location to get recommendations", Toast.LENGTH_SHORT).show();
        } else {
            Helper.hideKeyboard(this);
            recommendRestaurant.runRecommendation(foodDictator, restaurantZipCode.getText().toString());
        }
    }


    public void displayRecommendedRestaurants(List<Restaurant> recommendedRestaurants) {
        spinner.setVisibility(View.GONE);
        if (recommendedRestaurants.size() == 0) {
            this.restaurantRecommendationTitle.setVisibility(View.GONE);
            Toast.makeText(this, "No restaurants found, please select another location",
                    Toast.LENGTH_SHORT).show();
        } else {
            this.restaurantRecommendationTitle.setVisibility(View.VISIBLE);
            RestaurantAdapter playerAdapter = new RestaurantAdapter(this, recommendedRestaurants);
            allRestaurantsList.setAdapter(playerAdapter);
            allRestaurantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        final int position, long id) {
                    TextView selectedCategory = (TextView) view.findViewById(R.id.restaurant_category);
                    TextView selectedName = (TextView) view.findViewById(R.id.restaurant_name);
                    lunchBuddySingleton.addFoodDictatorRecommendation(
                            selectedCategory.getText().toString(), foodDictator);
                    sendIntent(selectedName.getText().toString());
                }
            });
        }
    }

    private void initComponents() {
        recommendRestaurant = new RecommendRestaurant(this);
        lunchBuddySingleton = LunchBuddySingleton.getInstance();
    }

    private void drawComponents() {
        foodDictatorName = (TextView) findViewById(R.id.food_dictator_name);
        this.restaurantRecommendationTitle = (TextView) findViewById(R.id.recommendation_title);
        restaurantZipCode = (EditText) findViewById(R.id.restaurant_location);
        allRestaurantsList = (ListView) findViewById(R.id.all_restaurants);
        recommendBtn = (Button) findViewById(R.id.recommend);
        spinner = (ProgressBar)findViewById(R.id.restaurant_progress_bar);
    }

    private void readIntent() {
        if (getIntent().hasExtra("foodDictator")) {
            foodDictator = (Player)getIntent().getSerializableExtra("foodDictator");
            String winnerMessage = String.format("Congratulations!! the winner is " +
                    "%s", foodDictator.getName());
            foodDictatorName.setText(winnerMessage);
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "foodDictator");
        }
    }

    private void sendIntent(String restaurantName) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("selectedRestaurantName", restaurantName);
        startActivity(intent);
    }
}
