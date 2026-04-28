package com.brewbite.model;

import com.brewbite.observer.Observer;
import com.brewbite.observer.Subject;

import java.util.*;

public class Inventory implements Subject {

    private Map<String, InventoryItem> items = new HashMap<>();
    private List<Observer> observers = new ArrayList<>();

    public void addItem(String name, int quantity) {
        items.put(name, new InventoryItem(name, quantity));
        notifyObservers("Inventory updated: " + name + " added/restocked");
    }

    public boolean isAvailable(String name, int required) {
        return items.containsKey(name) && items.get(name).getQuantity() >= required;
    }

    public void deduct(String name, int amount) {
        if (items.containsKey(name)) {
            items.get(name).deduct(amount);
            notifyObservers("Inventory updated: " + name + " deducted");
        }
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
