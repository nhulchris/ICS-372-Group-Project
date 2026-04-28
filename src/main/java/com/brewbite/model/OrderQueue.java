package com.brewbite.model;

import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {

    private Queue<Order> orders = new LinkedList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order getNextOrder() {
        return orders.poll();
    }
}
