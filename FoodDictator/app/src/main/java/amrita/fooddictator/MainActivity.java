package amrita.fooddictator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import amrita.fooddictator.Adapters.PlayerAdapter;
import amrita.fooddictator.Objects.LunchBuddySingleton;
import amrita.fooddictator.Objects.Player;

public class MainActivity extends AppCompatActivity {

    public ListView allPlayersList;
    public Button winnerBtn;
    public LunchBuddySingleton lunchBuddySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        drawComponents();
        PlayerAdapter playerAdapter = new PlayerAdapter(this, lunchBuddySingleton.getAllPlayers());
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
                if (presentPlayer.getText().toString().equals("SELECT")) {
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
        Intent intent = new Intent(this, RecommendationActivity.class);
        intent.putExtra("foodDictator", foodDictator);
        startActivity(intent);
    }
}
