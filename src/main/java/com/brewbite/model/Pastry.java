package com.brewbite.model;

import java.util.Map;

public class Pastry extends MenuItem {

    public Pastry(String name, double basePrice, Map<String, Integer> ingredients) {
        super(name, basePrice, ingredients);
    }

    @Override
    public double calculatePrice() {
        return basePrice;
    }
}
