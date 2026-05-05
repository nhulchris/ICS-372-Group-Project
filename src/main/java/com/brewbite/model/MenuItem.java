package com.brewbite.model;

import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for items that can appear on the menu.
 * Subclasses (Beverage, Pastry) define how the price is calculated.
 *
 * Demonstrates Inheritance: Beverage and Pastry share name, basePrice,
 * and ingredient consumption while specializing pricing behavior.
 */
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

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Polymorphic price calculation overridden by each subclass.
     */
    public abstract double calculatePrice();

    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", calculatePrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem that)) return false;
        return Double.compare(that.basePrice, basePrice) == 0
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, basePrice);
    }
}
