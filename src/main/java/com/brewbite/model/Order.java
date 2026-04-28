package com.brewbite.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderItem> items;
    private String status;

    public Order() {
        items = new ArrayList<>();
        status = "Pending";
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getItems() {
    return items;
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
