package com.brewbite.observer;

import com.brewbite.controller.BaristaController;

public class BaristaObserver implements Observer {

    private BaristaController controller;

    public BaristaObserver(BaristaController controller) {
        this.controller = controller;
    }

    @Override
    public void update(String message) {
        System.out.println("Barista notified: " + message);

        if (controller != null) {
            controller.addOrderToView("New Order Received");
        }
    }
}
