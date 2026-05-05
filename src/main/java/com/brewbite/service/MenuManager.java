package com.brewbite.service;

import com.brewbite.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject in the Observer pattern for the menu.
 * Holds the in-memory list of MenuItem objects the application is using
 * and notifies any registered MenuObserver whenever the menu changes.
 */
public class MenuManager {

    private final List<MenuItem> menu;
    private final List<MenuObserver> observers = new ArrayList<>();

    public MenuManager(List<MenuItem> menu) {
        this.menu = new ArrayList<>(menu);
    }

    // ---- Observer support ----
    public void addObserver(MenuObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MenuObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers that the menu has changed.
     * Public so controllers can trigger a refresh after editing an
     * existing MenuItem's fields in-place (e.g., the manager's
     * "Modify Item" feature).
     */
    public void notifyMenuChanged() {
        for (MenuObserver o : observers) {
            o.updateMenu(menu);
        }
    }

    // ---- Core ----
    public List<MenuItem> getMenu() {
        return menu;
    }

    public void addItem(MenuItem item) {
        menu.add(item);
        notifyMenuChanged();
    }

    public void removeItem(MenuItem item) {
        menu.remove(item);
        notifyMenuChanged();
    }

    public MenuItem findByName(String name) {
        return menu.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
