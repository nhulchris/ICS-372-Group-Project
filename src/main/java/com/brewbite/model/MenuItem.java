package com.brewbite.model;

import java.util.Map;

public abstract class MenuItem {

    protected String name;
    protected double basePrice;
    protected Map<String, Integer> ingredients;

    public MenuItem(String name, double basePrice, Map<String, Integer> ingredients) {
        this.name = name;
        this.basePrice = basePrice;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", calculatePrice());
    }

    public abstract double calculatePrice();
}
