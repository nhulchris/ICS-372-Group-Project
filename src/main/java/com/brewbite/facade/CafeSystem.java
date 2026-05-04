package com.brewbite.facade;

import com.brewbite.model.*;
import com.brewbite.persistence.DataLoader;
import com.brewbite.service.*;
import com.brewbite.util.DataStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class CafeSystem {

    private static CafeSystem instance;

    private OrderManager orderManager;
    private InventoryManager inventoryManager;
    private MenuManager menuManager;
    private String currentUser;

    public static CafeSystem getInstance() {
        if (instance == null) {
            instance = new CafeSystem();
        }
        return instance;
    }

    private CafeSystem() {

        Inventory inventory = DataLoader.loadInventory("/inventory.json");
        List<MenuItem> menu = DataLoader.loadMenu("/menu.json");

        this.orderManager = new OrderManager();
        this.inventoryManager = new InventoryManager(inventory);
        this.menuManager = new MenuManager(menu);

        Type orderType = new TypeToken<List<Order>>() {}.getType();
        List<Order> savedOrders = DataStore.loadList("orders.json", orderType);

        if (savedOrders != null) {
            for (Order o : savedOrders) {
                orderManager.addOrder(o);
            }
        }

        int maxId = savedOrders.stream()
                .mapToInt(Order::getOrderId)
                .max()
                .orElse(0);

        Order.setCounter(maxId + 1);
    }
    
    public List<MenuItem> getMenuItems() {
        return menuManager.getMenu();
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public boolean placeOrder(Order order) {

        if (!inventoryManager.hasIngredients(order)) {
            return false;
        }

        inventoryManager.consumeIngredients(order);
        orderManager.addOrder(order);
        order.setStatus(OrderStatus.IN_PROGRESS);

        return true;
    }

    public void restockIngredient(String name, int amount) {
        inventoryManager.restock(name, amount);
    }

    public void updateOrderStatus(int id, OrderStatus status) {
        orderManager.updateStatus(id, status);
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public void addMenuItem(MenuItem item) {
        menuManager.addItem(item);
    }

    public void removeMenuItem(MenuItem item) {
        menuManager.removeItem(item);
    }

    private String currentCustomerName;

    public void setCurrentCustomerName(String name) {
        this.currentCustomerName = name;
    }

    public String getCurrentCustomerName() {
        return currentCustomerName;
    }

    public Order createOrder(String customerName) {
        return new Order(customerName);
    }

    public void saveData() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        DataStore.saveText("orders.json", gson.toJson(orderManager.getOrders()));
        DataStore.saveText("inventory.json", gson.toJson(inventoryManager.getIngredients()));
        DataStore.saveText("menu.json", gson.toJson(menuManager.getMenu()));
    }
}