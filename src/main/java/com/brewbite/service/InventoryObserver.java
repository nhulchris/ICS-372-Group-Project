package com.brewbite.service;

import com.brewbite.model.Ingredient;
import java.util.Map;

public interface InventoryObserver {
    void updateInventory(Map<String, Ingredient> ingredients);
}