package com.brewbite.model;

import java.util.List;
import java.util.Map;

public class Beverage extends MenuItem {

    private Size size;
    private List<Customization> customizations;

    public Beverage(String name, double basePrice, Map<String, Integer> ingredients, Size size, List<Customization> customizations) {
        super(name, basePrice, ingredients);
        this.size = size;
        this.customizations = customizations;
    }

    @Override
    public double calculatePrice() {
        double total = basePrice;

        if (size != null) {
            switch (size) {
                case MEDIUM -> total += 1.0;
                case LARGE -> total += 2.0;
                case SMALL -> total += 0.0;
            }
        }

        if (customizations != null) {
            for (Customization c : customizations) {
                total += c.getPrice();
            }
        }

        return total;
    }

    public List<Customization> getCustomizations() {
        return customizations;
    }
}
