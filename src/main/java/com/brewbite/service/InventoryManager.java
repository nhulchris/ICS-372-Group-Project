package com.brewbite.service;

import com.brewbite.model.*;

import java.util.*;

public class InventoryManager {

    private Inventory inventory;

    // =====================
    // OBSERVER SUPPORT
    // =====================
    private List<InventoryObserver> observers = new ArrayList<>();

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (InventoryObserver o : observers) {
            o.updateInventory(inventory.getIngredients());
        }
    }

    public InventoryManager(Inventory inventory) {
        this.inventory = inventory;
    }

    // =====================
    // CORE LOGIC
    // =====================

    public boolean hasIngredients(Order order) {

        for (OrderItem item : order.getItems()) {

            for (Map.Entry<String, Integer> req : item.getItem().getIngredients().entrySet()) {

                Ingredient inv = inventory.getIngredient(req.getKey());

                if (inv == null || inv.getQuantity() < req.getValue() * item.getQuantity()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void consumeIngredients(Order order) {

        for (OrderItem item : order.getItems()) {

            for (Map.Entry<String, Integer> req : item.getItem().getIngredients().entrySet()) {

                inventory.reduceIngredient(
                        req.getKey(),
                        req.getValue() * item.getQuantity()
                );
            }
        }

        notifyObservers(); // 🔥 IMPORTANT
    }

    public void restock(String name, int amount) {
        inventory.addStock(name, amount);
        notifyObservers(); // 🔥 IMPORTANT
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Map<String, Ingredient> getIngredients() {
        return inventory.getIngredients();
    }
}