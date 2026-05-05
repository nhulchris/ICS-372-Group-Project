package com.brewbite.factory;

import com.brewbite.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory Method pattern.
 * Centralizes creation of MenuItem subclasses so callers don't need
 * to know about Beverage vs. Pastry construction details.
 *
 * To add a new menu item type, only this factory needs updating, plus the
 * new subclass — supporting the Open/Closed Principle.
 */
public class MenuItemFactory {

    /**
     * Generic factory entry point used by JSON loading and the manager UI.
     * Routes to the appropriate subclass constructor based on the type string.
     *
     * @param type "beverage" or "pastry" (case-insensitive)
     * @throws IllegalArgumentException if type is null or not recognized
     */
    public static MenuItem createMenuItem(
            String type,
            String name,
            double basePrice,
            Map<String, Integer> ingredients,
            Size size,
            List<Customization> customizations
    ) {
        if (type == null) {
            throw new IllegalArgumentException("Menu item type cannot be null");
        }

        Map<String, Integer> safeIngredients =
                (ingredients != null) ? ingredients : new HashMap<>();
        List<Customization> safeCustomizations =
                (customizations != null) ? customizations : new ArrayList<>();

        return switch (type.toLowerCase()) {
            case "beverage" ->
                    new Beverage(name, basePrice, safeIngredients, size, safeCustomizations);

            case "pastry" ->
                    new Pastry(name, basePrice, safeIngredients);

            default ->
                    throw new IllegalArgumentException(
                            "Unknown menu item type: '" + type + "'. Expected 'beverage' or 'pastry'.");
        };
    }

    /**
     * Type-safe convenience method for creating beverages.
     * Use this when the calling code already knows it wants a Beverage —
     * avoids the string-based dispatch and gets a Beverage return type.
     */
    public static Beverage createBeverage(String name,
                                          double basePrice,
