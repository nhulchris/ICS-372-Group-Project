package com.brewbite.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A beverage menu item. Inherits the basic name/price/ingredients from
 * MenuItem and adds Size and a list of Customizations.
 *
 * Size adjusts the price (Small +$0, Medium +$1, Large +$2) and
 * customizations stack additional cost on top.
 */
public class Beverage extends MenuItem {

    private Size size;
    private List<Customization> customizations;

    public Beverage(String name,
                    double basePrice,
                    Map<String, Integer> ingredients,
                    Size size,
                    List<Customization> customizations) {
        super(name, basePrice, ingredients);
        this.size = size;
        this.customizations = (customizations != null) ? customizations : new ArrayList<>();
    }

    @Override
    public double calculatePrice() {
        double total = basePrice;

        if (size != null) {
            switch (size) {
                case MEDIUM -> total += 1.0;
                case LARGE  -> total += 2.0;
                case SMALL  -> total += 0.0;
            }
        }

        for (Customization c : customizations) {
            total += c.getPrice();
        }

        return total;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<Customization> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(List<Customization> customizations) {
        this.customizations = (customizations != null) ? customizations : new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        if (size != null) {
            sb.append(" (").append(size).append(")");
        }
        if (!customizations.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < customizations.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(customizations.get(i).getName());
            }
            sb.append("]");
        }
        sb.append(" - $").append(String.format("%.2f", calculatePrice()));
        return sb.toString();
    }
}
