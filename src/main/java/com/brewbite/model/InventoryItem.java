package com.brewbite.model;

public class InventoryItem {

    private String name;
    private int quantity;

    public InventoryItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void deduct(int amount) {
        quantity -= amount;
    }

    public int getQuantity() {
        return quantity;
    }
}
