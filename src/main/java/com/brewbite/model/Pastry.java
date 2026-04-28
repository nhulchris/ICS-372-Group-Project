package com.brewbite.model;

public class Pastry extends MenuItem {

    public Pastry(String name, double basePrice) {
        super(name, basePrice);
    }

    @Override
    public double getPrice() {
        return basePrice;
    }
}
