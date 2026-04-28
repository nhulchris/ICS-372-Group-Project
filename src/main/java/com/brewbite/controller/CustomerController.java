package com.brewbite.controller;

import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;
import com.brewbite.observer.BaristaObserver;
import com.brewbite.observer.ManagerObserver;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CustomerController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<String> orderListView;

    private Order currentOrder = new Order();
    private OrderQueue orderQueue = new OrderQueue();
    private Inventory inventory = new Inventory();

    @FXML
    public void initialize() {

        // Load menu (temporary hardcoded)
        menuListView.setItems(FXCollections.observableArrayList(
                "Latte",
                "Espresso",
                "Croissant",
                "Muffin"
        ));

        // Attach observers
        orderQueue.addObserver(new BaristaObserver());
        inventory.addObserver(new ManagerObserver());

        // Initialize inventory
        inventory.addItem("CoffeeBeans", 5);
        inventory.addItem("Flour", 5);
    }

    @FXML
    public void handleAddToOrder() {

        String selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        boolean available;

        // Check inventory
        if (selected.equalsIgnoreCase("Latte") || selected.equalsIgnoreCase("Espresso")) {
            available = inventory.isAvailable("CoffeeBeans", 1);
        } else {
            available = inventory.isAvailable("Flour", 1);
        }

        if (!available) {
            System.out.println("Item out of stock: " + selected);
            return;
        }

        MenuItem item;

        // Create item + deduct inventory
        if (selected.equalsIgnoreCase("Latte") || selected.equalsIgnoreCase("Espresso")) {
            item = MenuItemFactory.createItem("beverage", selected, 3.0);
            inventory.deduct("CoffeeBeans", 1);
        } else {
            item = MenuItemFactory.createItem("pastry", selected, 2.0);
            inventory.deduct("Flour", 1);
        }

        OrderItem orderItem = new OrderItem(item, 1);
        currentOrder.addItem(orderItem);

        orderListView.getItems().add(selected + " - $" + item.getPrice());
    }

    @FXML
    public void handlePlaceOrder() {

        double total = currentOrder.calculateTotal();

        System.out.println("Order sent to queue. Total: $" + total);

        orderQueue.addOrder(currentOrder);

        orderListView.getItems().clear();
        currentOrder = new Order();
    }
}
