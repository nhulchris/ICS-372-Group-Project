package com.brewbite.factory;

import java.util.*;
import com.brewbite.model.*;

public class MenuItemFactory {

    public static MenuItem createMenuItem(
            String type,
            String name,
            double basePrice,
            Map<String, Integer> ingredients,
            Size size,
            List<Customization> customizations
    ) {

        return switch (type.toLowerCase()) {

            case "beverage" ->
                    new Beverage(name, basePrice, ingredients, size, customizations);

            case "pastry" ->
                    new Pastry(name, basePrice, ingredients);

            default ->
                    throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}