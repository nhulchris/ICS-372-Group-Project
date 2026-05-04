package com.brewbite.service;

import com.brewbite.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    private List<MenuItem> menu;

    // =====================
    // OBSERVER SUPPORT
    // =====================
    private List<MenuObserver> observers = new ArrayList<>();

    public void addObserver(MenuObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MenuObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (MenuObserver o : observers) {
            o.updateMenu(menu);
        }
    }

    public MenuManager(List<MenuItem> menu) {
        this.menu = new ArrayList<>(menu);
    }

    // =====================
    // CORE
    // =====================

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void addItem(MenuItem item) {
        menu.add(item);
        notifyObservers();
    }

    public void removeItem(MenuItem item) {
        menu.remove(item);
        notifyObservers();
    }

    public MenuItem findByName(String name) {
        return menu.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}