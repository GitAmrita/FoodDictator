package amrita.fooddictator.Listeners;

/**
 * Created by amritachowdhury on 8/9/17.
 */
/* Functionalities:
1. This class contains the listener when the access token is returned by the yelp access token api.
* */
public interface GetYelpAccessTokenListener {
    void getYelpAccessToken(String token, int expiresIn);
}


