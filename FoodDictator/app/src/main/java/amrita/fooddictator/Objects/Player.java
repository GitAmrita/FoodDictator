package amrita.fooddictator.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by amritachowdhury on 8/9/17.
 */

public class Player implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Map<String, Integer> recommendations) {
        this.recommendations = recommendations;
    }

    public void addRecommendation(String category) {
        Integer selectionFrequency = this.recommendations.get(category);
        if (selectionFrequency == null) {
            this.recommendations.put(category, 1);
        } else {
            this.recommendations.put(category, 1 + selectionFrequency);
        }
    }

    private int id;
    private String name;
    private Map<String, Integer> recommendations;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.recommendations = new HashMap<>();
    }

    public LinkedHashMap<String, Integer> sortRecommendationsBasedOnPopularity() {
        List<String> mapKeys = new ArrayList<>(this.recommendations.keySet());
        List<Integer> mapValues = new ArrayList<>(this.recommendations.values());
        Collections.sort(mapValues, Collections.<Integer>reverseOrder());
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = this.recommendations.get(key);
                Integer comp2 = val;

                if (comp1 == comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
