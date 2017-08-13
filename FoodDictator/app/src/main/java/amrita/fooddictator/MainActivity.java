package amrita.fooddictator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import amrita.fooddictator.Adapters.PlayerAdapter;
import amrita.fooddictator.Objects.LunchBuddySingleton;
import amrita.fooddictator.Objects.Player;

/*
* Functionalities of the screen
* 1. Select or deselect the players by clicking from the persistent list of players displayed in the app
* 2. Click the button to get the Food Dictator on the next screen
* 3. If the button is clicked w/o selecting any player, error toast is displayed
* */

public class MainActivity extends AppCompatActivity {

    public ListView allPlayersList;
    public Button winnerBtn;
    public LunchBuddySingleton lunchBuddySingleton;
    public PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        drawComponents();
        playerAdapter = new PlayerAdapter(this, lunchBuddySingleton.getAllPlayers());
        allPlayersList.setAdapter(playerAdapter);
        allPlayersList.addFooterView(winnerBtn);
        allPlayersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    final int position, long id) {
                TextView presentPlayer = (TextView) view.findViewById(R.id.player_present);
                TextView playerId = (TextView) view.findViewById(R.id.player_id);
                Player selectedPlayer = lunchBuddySingleton.getPlayerById(
                        Integer.parseInt(playerId.getText().toString()));
                if (!selectedPlayer.isSelected()) {
                    presentPlayer.setText("SELECTED");
                    lunchBuddySingleton.addPresentPlayer(selectedPlayer);
                } else {
                    presentPlayer.setText("SELECT");
                    lunchBuddySingleton.removePresentPlayer(selectedPlayer);
                }
            }
        });
    }

    private void initComponent() {
        lunchBuddySingleton = LunchBuddySingleton.getInstance();
    }

    private void drawComponents() {
        allPlayersList = (ListView) findViewById(R.id.all_players);
        allPlayersList.setItemsCanFocus(true);
        View view = getLayoutInflater().inflate(R.layout.list_players_footer, null);
        winnerBtn = (Button) view.findViewById(R.id.winner);
    }

    public void onClickWinnerBtn(View v) {
        Player foodDictator = lunchBuddySingleton.getFoodDictator();
        if (foodDictator != null) {
            Intent intent = new Intent(this, RecommendationActivity.class);
            intent.putExtra("foodDictator", foodDictator);
            startActivityForResult(intent, Config.RESET_PRESENT_PLAYERS_RESULT_CODE);
        } else {
            Toast.makeText(this, "Please select at least one player from the list",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Config.RESET_PRESENT_PLAYERS_RESULT_CODE) {
            lunchBuddySingleton.resetAllPresentPlayers();
            runOnUiThread(new Runnable() {
                public void run() {
                    playerAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
