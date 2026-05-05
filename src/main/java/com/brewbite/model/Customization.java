package com.brewbite.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents an optional add-on for a beverage (e.g., extra shot, oat milk).
 * Carries its own price surcharge and ingredient consumption.
 *
 * Modeled as a "data class" — its main purpose is to hold data, with
 * standard accessors plus value-based equals/hashCode and a readable toString.
 */
public class Customization {

    private final String name;
    private final double price;
    private final Map<String, Integer> ingredients;

    public Customization(String name, double price, Map<String, Integer> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return name + " (+$" + String.format("%.2f", price) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customization that)) return false;
        return Double.compare(that.price, price) == 0
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
