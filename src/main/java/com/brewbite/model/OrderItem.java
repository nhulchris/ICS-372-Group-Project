package com.brewbite.model;

/**
 * One line item in an Order — pairs a MenuItem with a quantity
 * and computes the subtotal price for that line.
 */
public class OrderItem {

    private MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return item.calculatePrice() * quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return item.toString() + "  x" + quantity
                + "   ($" + String.format("%.2f", getSubtotal()) + ")";
    }
}
