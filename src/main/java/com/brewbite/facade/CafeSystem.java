package com.brewbite.facade;

import com.brewbite.model.*;
import com.brewbite.persistence.DataLoader;
import com.brewbite.persistence.MenuItemAdapter;
import com.brewbite.service.*;
import com.brewbite.util.DataStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CafeSystem is the Facade that controllers interact with.
 * It owns the OrderManager, InventoryManager, and MenuManager and
 * coordinates business operations between them.
 *
 * Implements the Facade and Singleton patterns.
 */
public class CafeSystem {

    private static CafeSystem instance;

    private final OrderManager orderManager;
    private final InventoryManager inventoryManager;
    private final MenuManager menuManager;

    private String currentCustomerName;

    /**
     * Gson instance configured with the custom adapter for the abstract
     * MenuItem class. Used everywhere we serialize/deserialize MenuItem
     * (or anything containing one, e.g., Order -> OrderItem -> MenuItem).
     */
    private static final Gson menuAwareGson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(MenuItem.class, new MenuItemAdapter())
            .create();

    public static CafeSystem getInstance() {
        if (instance == null) {
            instance = new CafeSystem();
        }
        return instance;
    }

    private CafeSystem() {
        Inventory inventory = loadInventoryWithFallback();
        List<MenuItem> menu = loadMenuWithFallback();

        this.orderManager = new OrderManager();
        this.inventoryManager = new InventoryManager(inventory);
        this.menuManager = new MenuManager(menu);

        loadSavedOrders();
    }

    /**
     * Loads the user's persisted inventory from ~/brewbite-data/inventory.json
     * if present; otherwise falls back to the bundled default in resources.
     */
    private Inventory loadInventoryWithFallback() {
        Type mapType = new TypeToken<Map<String, Ingredient>>() {}.getType();
        Map<String, Ingredient> saved = DataStore.loadObject("inventory.json", mapType);

        if (saved != null && !saved.isEmpty()) {
            Inventory inv = new Inventory();
            for (Map.Entry<String, Ingredient> e : saved.entrySet()) {
                inv.addStock(e.getKey(), e.getValue().getQuantity());
            }
            return inv;
        }
        return DataLoader.loadInventory("/inventory.json");
    }

    /**
     * Loads the user's persisted menu from ~/brewbite-data/menu.json if present;
     * otherwise falls back to the bundled default in resources.
     */
    private List<MenuItem> loadMenuWithFallback() {
        Type listType = new TypeToken<List<MenuItem>>() {}.getType();
        List<MenuItem> saved = DataStore.loadList("menu.json", listType, menuAwareGson);

        if (saved != null && !saved.isEmpty()) {
            return saved;
        }

        List<MenuItem> menu = DataLoader.loadMenu("/menu.json");
        if (menu == null) menu = new ArrayList<>();
        return menu;
    }

    private void loadSavedOrders() {
        Type orderType = new TypeToken<List<Order>>() {}.getType();
        List<Order> savedOrders = DataStore.loadList("orders.json", orderType, menuAwareGson);

        if (savedOrders == null || savedOrders.isEmpty()) {
            return;
        }

        for (Order o : savedOrders) {
            orderManager.addOrder(o);
        }

        int maxId = savedOrders.stream()
                .mapToInt(Order::getOrderId)
                .max()
                .orElse(0);

        Order.setCounter(maxId + 1);
    }

    // ---- Menu ----
    public List<MenuItem> getMenuItems() {
        return menuManager.getMenu();
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public void addMenuItem(MenuItem item) {
        menuManager.addItem(item);
    }

    public void removeMenuItem(MenuItem item) {
        menuManager.removeItem(item);
    }

    // ---- Orders ----
    public Order createOrder(String customerName) {
        return new Order(customerName);
    }

    public boolean placeOrder(Order order) {
        if (order == null || order.getItems().isEmpty()) {
            return false;
        }
        if (!inventoryManager.hasIngredients(order)) {
            return false;
        }

        inventoryManager.consumeIngredients(order);
        order.setStatus(OrderStatus.PENDING);
        orderManager.addOrder(order);
        return true;
    }

    public void updateOrderStatus(int id, OrderStatus status) {
        orderManager.updateStatus(id, status);
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    // ---- Inventory ----
    public void restockIngredient(String name, int amount) {
        inventoryManager.restock(name, amount);
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    // ---- Customer name ----
    public void setCurrentCustomerName(String name) {
        this.currentCustomerName = name;
    }

    public String getCurrentCustomerName() {
        return currentCustomerName;
    }

    // ---- Persistence ----
    public void saveData() {
        DataStore.saveText("orders.json",    menuAwareGson.toJson(orderManager.getOrders()));
        DataStore.saveText("inventory.json", menuAwareGson.toJson(inventoryManager.getIngredients()));
        DataStore.saveText("menu.json",      menuAwareGson.toJson(menuManager.getMenu()));
    }
}
