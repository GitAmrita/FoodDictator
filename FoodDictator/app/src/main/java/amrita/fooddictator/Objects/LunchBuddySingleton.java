package amrita.fooddictator.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by amritachowdhury on 8/11/17.
 */
/* Functionalities:
1. This class contains the persistent list of players.
2. Generates the food Dictator from the list of present players. The dictator is selected randomly.
The value is recalculated if it is same as the last time.
3. This is a singleton class so that we can create only one instance of it. Since we don't have a
data source in this project, so keeping everything in memory. If we create more than one instance, we
will loose all players previous selections and hence the recommendation engine will perform poorly.
* */

public class LunchBuddySingleton {
    private static LunchBuddySingleton ourInstance;
    private static String lastWinner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(ArrayList<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

    private ArrayList<Player> allPlayers;

    public static LunchBuddySingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new LunchBuddySingleton();
        }
        return ourInstance;
    }

    private LunchBuddySingleton() {
        this.allPlayers = new ArrayList<>();
        this.name = "VivLabsHackers";
        this.lastWinner = "";
        createTeam();
    }

    private void createTeam() {
        Player p1 = new Player(1, "Paulina Ng");
        Player p2 = new Player(2, "Sarah Moliner-Roy");
        Player p3 = new Player(3, "Siamak Hodjat");
        Player p4 = new Player(4, "Brad Melluish");
        Player p5 = new Player(5, "Andrew Roberts");
        Player p6 = new Player(6, "Adam Cheyer");
        Player p7 = new Player(7, "Adam Elconin");
        Player p8 = new Player(8, "Amrita Chowdhury");

        allPlayers.add(p1); allPlayers.add(p2); allPlayers.add(p3); allPlayers.add(p4);
        allPlayers.add(p5); allPlayers.add(p6); allPlayers.add(p7); allPlayers.add(p8);
    }

    public Player getFoodDictator() {
        List<Player> presentPlayers = getPresentPlayers();
        if (presentPlayers.size() == 0) {
            return null;
        }
        else if (presentPlayers.size() == 1) {
            lastWinner = presentPlayers.get(0).getName();
            return presentPlayers.get(0);
        } else {
            return getFoodDictator(presentPlayers);
        }
    }

    private Player getFoodDictator(List<Player> presentPlayers) {
        Random rand = new Random();
        int randIdx = rand.nextInt(presentPlayers.size());
        if (lastWinner.equals(presentPlayers.get(randIdx).getName())) {
            return getFoodDictator(presentPlayers);
        } else {
            lastWinner = presentPlayers.get(randIdx).getName();
            return presentPlayers.get(randIdx);
        }
    }

    public void addFoodDictatorRecommendation(String category, Player foodDictator) {
        for (Player p : allPlayers) {
            if (p.getId() == foodDictator.getId()) {
                p.addRecommendation(category);
                break;
            }
        }
    }

    public Player getPlayerById(int id) {
        for (Player p : allPlayers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void addPresentPlayer(Player presentPlayer) {
        presentPlayer.setSelected(true);
    }

    public void removePresentPlayer(Player presentPlayer) {
        presentPlayer.setSelected(false);
    }

    public void resetAllPresentPlayers() {
        for (Player p : this.allPlayers) {
            removePresentPlayer(p);
        }
    }

    private List<Player> getPresentPlayers() {
        List<Player> presentPlayers = new ArrayList<>(this.allPlayers.size());
        for (Player p : this.allPlayers) {
            if (p.isSelected()) {
                presentPlayers.add(p);
            }
        }
        return presentPlayers;
    }
}
