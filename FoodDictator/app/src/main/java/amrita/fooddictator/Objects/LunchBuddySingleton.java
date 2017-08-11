package amrita.fooddictator.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amritachowdhury on 8/11/17.
 */

public class LunchBuddySingleton {
    private static LunchBuddySingleton ourInstance;

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

    public List<Player> getPresentPlayers() {
        return presentPlayers;
    }

    public void setPresentPlayers(List<Player> presentPlayers) {
        this.presentPlayers = presentPlayers;
    }

    private ArrayList<Player> allPlayers;
    private List<Player> presentPlayers;

    public static LunchBuddySingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new LunchBuddySingleton();
        }
        return ourInstance;
    }

    private LunchBuddySingleton() {
        this.allPlayers = new ArrayList<>();
        this.presentPlayers = new ArrayList<>();
        this.name = "VivLabsHackers";
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
        /*Random rand = new Random();
        int randIdx = rand.nextInt(this.presentPlayers.size());
        return this.presentPlayers.get(randIdx);*/
        return this.allPlayers.get(0);
    }

    public void addFoodDictatorRecommendation(String category, Player foodDictator) {
        for (Player p : allPlayers) {
            if (p.getId() == foodDictator.getId()) {
                p.addRecommendation(category);
                break;
            }
        }
        for (Player p : presentPlayers) {
            if (p.getId() == foodDictator.getId()) {
                p.setRecommendations(getPlayerById(foodDictator.getId()).getRecommendations());
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
        this.presentPlayers.add(presentPlayer);
    }

    public void removePresentPlayer(Player presentPlayer) {
        this.presentPlayers.remove(presentPlayer);
    }
}
