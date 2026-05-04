package com.brewbite.service;

import com.brewbite.model.*;
import java.util.ArrayList;
import java.util.List;
public class OrderManager {

    private List<Order> orders = new ArrayList<>();
    private List<OrderObserver> observers = new ArrayList<>();

    // =====================
    // OBSERVER SUPPORT
    // =====================
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (OrderObserver o : observers) {
            o.updateOrders(List.copyOf(orders));
        }
    }

    // =====================
    // CORE
    // =====================
    public void addOrder(Order order) {
        orders.add(order);
        notifyObservers();
    }

    public void updateStatus(int orderId, OrderStatus status) {
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                o.setStatus(status);
                break;
            }
        }
        notifyObservers();
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }
}