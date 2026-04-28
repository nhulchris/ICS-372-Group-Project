package com.brewbite.system;

import com.brewbite.model.Inventory;
import com.brewbite.model.OrderQueue;

public class SystemState {

    private static final OrderQueue orderQueue = new OrderQueue();
    private static final Inventory inventory = new Inventory();

    public static OrderQueue getOrderQueue() {
        return orderQueue;
    }

    public static Inventory getInventory() {
        return inventory;
    }
}
