package com.brewbite.model;

import com.brewbite.observer.Observer;
import com.brewbite.observer.Subject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OrderQueue implements Subject {

    private Queue<Order> orders = new LinkedList<>();
    private List<Observer> observers = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
        notifyObservers("New order added");
    }

    public Order getNextOrder() {
        return orders.poll();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
