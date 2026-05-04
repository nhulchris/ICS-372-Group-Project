package com.brewbite.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private static int counter = 1;

    private int orderId;
    private String customerName;
    private List<OrderItem> items;
    private OrderStatus status;

    public Order(String customerName) {
        this.orderId = counter++;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public static void setCounter(int value) {
        counter = value;
    }

    public int getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }
    public String getCustomerName() {
        return customerName;
    }
    // =========================
    // UI DISPLAY FIX (IMPORTANT)
    // =========================
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Order #").append(orderId)
          .append(" | ").append(customerName)
          .append(" | ").append(status)
          .append("\n");

        for (OrderItem item : items) {
            sb.append("  - ")
              .append(item.getItem().getName())
              .append(" x")
              .append(item.getQuantity())
              .append("\n");
        }

        sb.append("Total: $")
          .append(String.format("%.2f", calculateTotal()));

        return sb.toString();
    }

    
}