package amrita.fooddictator;

/**
 * Created by amritachowdhury on 8/9/17.
 */

public class Config {
    public static final String YELP_CLIENT_ID = "Nqh_lt12HWGnj7JRQGsSPA";
    public static final String YELP_CLIENT_SECRET = "Kes6sp5ciDTd47XZHbyd0AtRusotBhcquATcErwY4CciqN1I1qNWnZw7gRv6CbkM";
    public static final int NUMBER_OF_DEFAULT_RESTAURANT_RESULTS = 10;
    public static final int NUMBER_OF_CUSTOM_RESTAURANT_RESULTS = 6;
    public static final String DEFAULT_SORT_BY_RESTAURANTS = "rating";
    public static final String DEFAULT_SEARCH_TERM = "restaurants";
    public static final int NO_OF_CATEGORIES_FOR_RESTAURANT_RECOMMENDATION = 3;
    public static final int YELP_ACCESS_TOKEN_EXPIRY_THRESHOLD = 2 * 24 * 60; // 2 days in seconds
    public static final int RESET_PRESENT_PLAYERS_RESULT_CODE = 1;
}
