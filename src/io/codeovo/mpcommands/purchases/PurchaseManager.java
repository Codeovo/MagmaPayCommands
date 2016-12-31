package io.codeovo.mpcommands.purchases;

import java.util.HashMap;
import java.util.List;

public class PurchaseManager {
    private HashMap<String, Integer> purchasesMap;

    public PurchaseManager() {
        this.purchasesMap = new HashMap<>();
    }

    public void loadMap(List<String> listToLoad) {
        for (String line : listToLoad) {
            String[] split = line.split(";");

            purchasesMap.put(split[0], Integer.parseInt(split[1]));
        }
    }

    public boolean itemExists(String item) { return purchasesMap.containsKey(item); }

    public int getItemPrice(String item) { return purchasesMap.get(item); }
}
