package com.brewbite.model;

import java.util.Map;

public class Customization {
    private String name;
    private double price;
    private Map<String, Integer> ingredients;

    public Customization(String name, double price, Map<String, Integer> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }
}
