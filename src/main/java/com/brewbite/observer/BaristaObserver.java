package com.brewbite.observer;

public class BaristaObserver implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Barista notified: " + message);
    }
}
