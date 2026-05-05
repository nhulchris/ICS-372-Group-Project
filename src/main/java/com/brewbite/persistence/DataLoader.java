package com.brewbite.persistence;

import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads the bundled default Inventory and Menu from JSON resource files.
 * On first run (when no persisted data exists in the user-home folder),
 * CafeSystem uses these defaults to seed the application.
 *
 * Each parsing concern (ingredients, size, customizations, variation) is
 * isolated in its own helper method to keep the loader focused.
 */
public class DataLoader {

    // ------------------------------------------------------------------
    // INVENTORY
    // ------------------------------------------------------------------
    public static Inventory loadInventory(String path) {
        Inventory inventory = new Inventory();

        try (InputStream in = DataLoader.class.getResourceAsStream(path)) {
            if (in == null) {
                System.err.println("Resource not found: " + path);
                return inventory;
            }

            JsonObject json = JsonParser
                    .parseReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .getAsJsonObject();

            JsonArray ingredients = json.getAsJsonArray("ingredients");
            if (ingredients == null) return inventory;

            for (JsonElement e : ingredients) {
                JsonObject obj = e.getAsJsonObject();
                String name  = obj.get("name").getAsString();
                int quantity = obj.get("quantity").getAsInt();
                inventory.addStock(name, quantity);
            }

        } catch (Exception e) {
            System.err.println("Error loading inventory from " + path + ": " + e.getMessage());
        }

        return inventory;
    }

    // ------------------------------------------------------------------
    // MENU
    // ------------------------------------------------------------------
    public static List<MenuItem> loadMenu(String path) {
        List<MenuItem> menu = new ArrayList<>();

        try (InputStream in = DataLoader.class.getResourceAsStream(path)) {
            if (in == null) {
                System.err.println("Resource not found: " + path);
                return menu;
            }

            JsonObject json = JsonParser
                    .parseReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                    .getAsJsonObject();

            JsonArray items = json.getAsJsonArray("menu");
            if (items == null) return menu;

            for (JsonElement e : items) {
                MenuItem item = parseMenuItem(e.getAsJsonObject());
                if (item != null) menu.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error loading menu from " + path + ": " + e.getMessage());
        }

        return menu;
    }

    // ------------------------------------------------------------------
    // PARSING HELPERS
    // ------------------------------------------------------------------
    private static MenuItem parseMenuItem(JsonObject obj) {
        String type  = obj.get("type").getAsString();
        String name  = obj.get("name").getAsString();
        double price = obj.get("price").getAsDouble();

        Map<String, Integer> ingredients = parseIngredients(obj);

        if ("beverage".equalsIgnoreCase(type)) {
            Size size = parseSize(obj);
            List<Customization> customizations = parseCustomizations(obj);
            return MenuItemFactory.createBeverage(name, price, ingredients, size, customizations);
        }

        if ("pastry".equalsIgnoreCase(type)) {
            String variation = obj.has("variation")
                    ? obj.get("variation").getAsString()
                    : null;
            return MenuItemFactory.createPastry(name, price, ingredients, variation);
        }

        System.err.println("Unknown menu item type in JSON: " + type);
        return null;
    }

    private static Map<String, Integer> parseIngredients(JsonObject obj) {
        Map<String, Integer> ingredients = new HashMap<>();
        if (!obj.has("ingredients")) return ingredients;

        JsonObject ingObj = obj.getAsJsonObject("ingredients");
        for (String key : ingObj.keySet()) {
            ingredients.put(key, ingObj.get(key).getAsInt());
        }
        return ingredients;
    }

    private static Size parseSize(JsonObject obj) {
        if (!obj.has("size")) return Size.SMALL; // sensible default
        try {
            return Size.valueOf(obj.get("size").getAsString().toUpperCase());
        } catch (Exception ex) {
            return Size.SMALL;
        }
    }

    private static List<Customization> parseCustomizations(JsonObject obj) {
        List<Customization> list = new ArrayList<>();
        if (!obj.has("customizations")) return list;

        JsonArray arr = obj.getAsJsonArray("customizations");
        for (JsonElement e : arr) {
            JsonObject c = e.getAsJsonObject();
            String cName  = c.get("name").getAsString();
            double cPrice = c.has("price") ? c.get("price").getAsDouble() : 0.0;

            Map<String, Integer> cIngredients = new HashMap<>();
            if (c.has("ingredients")) {
                JsonObject ingObj = c.getAsJsonObject("ingredients");
                for (String key : ingObj.keySet()) {
                    cIngredients.put(key, ingObj.get(key).getAsInt());
                }
            }
            list.add(new Customization(cName, cPrice, cIngredients));
        }
        return list;
    }
}
