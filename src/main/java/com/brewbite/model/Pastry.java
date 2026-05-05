package com.brewbite.model;

import java.util.Map;

/**
 * A pastry menu item. Pastries have a fixed base price and an optional
 * variation (e.g., "Butter" or "Chocolate" for a Croissant).
 *
 * Unlike Beverage, Pastry does not support sizes or runtime customizations —
 * variations are predefined in the menu data so each pastry kind can be
 * stocked and ordered independently.
 */
public class Pastry extends MenuItem {

    private String variation;

    public Pastry(String name, double basePrice, Map<String, Integer> ingredients) {
        this(name, basePrice, ingredients, null);
    }

    public Pastry(String name,
                  double basePrice,
                  Map<String, Integer> ingredients,
                  String variation) {
        super(name, basePrice, ingredients);
        this.variation = variation;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    @Override
    public double calculatePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        if (variation != null && !variation.isBlank()) {
            return variation + " " + name + " - $" + String.format("%.2f", calculatePrice());
        }
        return super.toString();
    }
}
