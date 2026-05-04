package com.brewbite.persistence;

import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;
import com.google.gson.*;

import java.io.InputStreamReader;
import java.util.*;

public class DataLoader {

    // =========================
    // INVENTORY
    // =========================
    public static Inventory loadInventory(String path) {

        Inventory inventory = new Inventory();

        try {
            JsonObject json = JsonParser.parseReader(
                    new InputStreamReader(DataLoader.class.getResourceAsStream(path))
            ).getAsJsonObject();

            JsonArray ingredients = json.getAsJsonArray("ingredients");

            for (JsonElement e : ingredients) {

                JsonObject obj = e.getAsJsonObject();

                String name = obj.get("name").getAsString();
                int quantity = obj.get("quantity").getAsInt();

                inventory.addStock(name, quantity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }

    // =========================
    // MENU (DOMAIN LIST ONLY)
    // =========================
    public static List<MenuItem> loadMenu(String path) {

        List<MenuItem> menu = new ArrayList<>();

        try {
            JsonObject json = JsonParser.parseReader(
                    new InputStreamReader(DataLoader.class.getResourceAsStream(path))
            ).getAsJsonObject();

            JsonArray items = json.getAsJsonArray("menu");

            for (JsonElement e : items) {

                JsonObject obj = e.getAsJsonObject();

                String type = obj.get("type").getAsString();
                String name = obj.get("name").getAsString();
                double price = obj.get("price").getAsDouble();

                Map<String, Integer> ingredients = new HashMap<>();

                if (obj.has("ingredients")) {
                    JsonObject ingObj = obj.getAsJsonObject("ingredients");

                    for (String key : ingObj.keySet()) {
                        ingredients.put(key, ingObj.get(key).getAsInt());
                    }
                }

                Size size = null;
                if (obj.has("size")) {
                    try {
                        size = Size.valueOf(obj.get("size").getAsString().toUpperCase());
                    } catch (Exception ignored) {}
                }

                List<Customization> customizations = new ArrayList<>();

                MenuItem item = MenuItemFactory.createMenuItem(
                        type,
                        name,
                        price,
                        ingredients,
                        size,
                        customizations
                );

                menu.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return menu;
    }

    public static List<Order> loadOrders(String path) {

        List<Order> orders = new ArrayList<>();

        try {
            JsonArray json = JsonParser.parseReader(
                    new InputStreamReader(DataLoader.class.getResourceAsStream(path))
            ).getAsJsonArray();

            Gson gson = new Gson();

            for (JsonElement e : json) {
                Order o = gson.fromJson(e, Order.class);
                orders.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
}