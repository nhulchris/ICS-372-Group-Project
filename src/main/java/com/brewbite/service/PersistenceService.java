package com.brewbite.service;

import com.brewbite.util.DataStore;

public class PersistenceService {

    public void saveInventorySnapshot(String inventoryData) {
        DataStore.saveText("inventory.json", inventoryData);
    }

    public void saveOrderSnapshot(String orderData) {
        DataStore.saveText("orders.json", orderData);
    }

    public String loadInventorySnapshot() {
        return DataStore.loadText("inventory.json");
    }

    public String loadOrderSnapshot() {
        return DataStore.loadText("orders.json");
    }
}
