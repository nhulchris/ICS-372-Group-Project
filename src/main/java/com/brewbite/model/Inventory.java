package com.brewbite.model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<String, InventoryItem> items = new HashMap<>();

    public void addItem(String name, int quantity) {
        items.put(name, new InventoryItem(name, quantity));
    }

    public boolean isAvailable(String name, int required) {
        return items.containsKey(name) && items.get(name).getQuantity() >= required;
    }

    public void deduct(String name, int amount) {
        if (items.containsKey(name)) {
            items.get(name).deduct(amount);
        }
    }
}
