# FoodDictator

Food dictator is an app which allows one to select the players to play a game of "Who's the food dictator for today's lunch".

1. Once the players are selected, the app generates the food dictator of the day. (Home Screen)

2. The dictator is asked to enter the location (zip/ city) where he wants to have lunch. Based on the location and the dictators previous restaurant picks, the app recommends a list of restaurants to select from.(2nd screen - restaurant recommendation)

3. Once the dictator clicks on the restaurant, a google map opens up for navigation.(3rd screen - google maps for the restaurant selected)

4. Once the user clicks the hardware back button, the game is reset.

# Recommendation Logic
Each player in the app has a list of previously visited restaurant cuisine types which is initially empty. When a player is selected as the food dictator, he picks a restaurant from the recommended list. This adds the cuisine type to the list of previously visited restaurant cuisine types. 
The recommendation is done through Yelp Search api. There are following two types of recommendations.

DEFAULT_RECOMMENDATION: This happens when the player has no previously visited restaurants. The search api returns a           list of different cuisine type restaurants. Number of results returned is NUMBER_OF_DEFAULT_RESTAURANT_RESULTS.
      
CUSTOM_RECOMMENDATION: This happens when the player has atleast one previously visited restaurant. For simplicity the         app considers the cuisine of only the top 3 restaurants visited by the player. The search api returns a list of restaurants           which belong to the specified cuisines. The number of such results is NUMBER_OF_CUSTOM_RESTAURANT_RESULTS. In addition         to that the engine also recommends some restaurants which belong to other cuisine types. Number of such recommendations       is NUMBER_OF_DEFAULT_RESTAURANT_RESULTS - NUMBER_OF_CUSTOM_RESTAURANT_RESULTS. AS a result, the player doesn't only get       recommended restaurants based on his previous choices.
    
# App Design
Since the app doesn't have any datasource, all actions are in memory. That means the persistent list of players to select from is stored in memory and is recreated every time the app is started.
The list of previously visited restaurants for each player also gets cleared when the app exits.

# Detailed Design
  __Activities__
  
   Main Activity
   
  1. Select or deselect the players by clicking from the persistent list of players displayed in the app.
  2. Click the button to get the Food Dictator on the next screen.
  3. If the button is clicked w/o selecting any player, error toast is displayed.
          
 Recommendation Activity
 
  1. Displays the food dictator by a random pick from the selected players from the list of persistent players.
  2. Food dictator enters the location (city/ zip) of the neighborhood he wants to go to.
  3. Click the button to get the list of recommended restaurants.
  4. If there are no recommended restaurants, a toast is displayed.
  5. On pressing the back button, all selected players are reset.
          
 WebView Activity
 
  1. Displays the google page for the selected restaurant from the list of recommended restaurants.
  
__Classes__:

  LunchBuddySingleton:
  
  1. This class contains the persistent list of players.
  2. Generates the food Dictator from the list of present players. The dictator is selected randomly.
     The value is recalculated if it is same as the last time.
  3. This is a singleton class so that we can create only one instance of it. Since we don't have a
     data source in this project, so keeping everything in memory. If we create more than one instance, we
     will loose all players previous selections and hence the recommendation engine will perform poorly.
     
  Player:
  
  1. This class contains the player details.
  2. Player has a id which is unique, a name and a list of his previous selections in the form of
    map < cuisine, no of times selected>. Initially it is empty.
    
  Restaurant:
  
  1. This class contains the restaurant details.
  
__Recommendation Engine__:

  RecommendRestaurants:
  
  1. Covered in the #recommendation logic section.
  
  YelpRestClient:
  
  1. REST api client from the library android-async-http:1.4.9
  
  YelpAccessTokenAPI:
  
  1. Gets the access token from yelp, to make further calls to yelp V3 REST api
  2. Validates the saved token since yelp token typically expire by certain time.
  
  YelpSearchAPI:
  
  1. Gets the list of restaurants based on query params (location, recommended category) using yelp's V3 REST api.
  
 __Listeners__:
 
  GetYelpAccessTokenListener:
  
  1. This class contains the listener when the access token is returned by the yelp access token api.
  
  GetYelpRestaurantRecommendationListener:
  
  1. This class contains the listener when the list of restaurants is returned by the yelp search api.
  
 __Adapters__:
 
  PlayerAdapter:
          
  1. Adapter for list of the persistent list of players.
          
  RestaurantAdapter:
          
  1. Adapter for list of recommended restaurants.
          
