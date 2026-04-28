package com.brewbite.model;

import java.util.List;

public class Beverage extends MenuItem {

    private String size;
    private List<String> customizations;

    public Beverage(String name, double basePrice, String size, List<String> customizations) {
        super(name, basePrice);
        this.size = size;
        this.customizations = customizations;
    }

    @Override
    public double getPrice() {
        double total = basePrice;

        if (size.equalsIgnoreCase("Medium")) {
            total += 1.0;
        } else if (size.equalsIgnoreCase("Large")) {
            total += 2.0;
        }

        total += customizations.size() * 0.5;

        return total;
    }
}
