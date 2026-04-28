package com.brewbite.factory;

import com.brewbite.model.*;

import java.util.List;

public class MenuItemFactory {

    public static MenuItem createItem(String type, String name, double price) {

        if (type.equalsIgnoreCase("beverage")) {
            return new Beverage(name, price, "Small", List.of());
        } else if (type.equalsIgnoreCase("pastry")) {
            return new Pastry(name, price);
        }

        throw new IllegalArgumentException("Invalid menu item type");
    }
}
