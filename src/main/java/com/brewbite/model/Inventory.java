package com.brewbite.model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private Map<String, Ingredient> ingredients = new HashMap<>();

    public Ingredient getIngredient(String name) {
        return ingredients.get(name);
    }

    public void addStock(String name, int amount) {
        ingredients.putIfAbsent(name, new Ingredient(name, 0));
        ingredients.get(name).addQuantity(amount);
    }

    public void reduceIngredient(String name, int amount) {
        if (ingredients.containsKey(name)) {
            Ingredient ing = ingredients.get(name);

            if (ing.getQuantity() >= amount) {
                ing.reduceQuantity(amount);
            }
        }
    }

    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    } 
}