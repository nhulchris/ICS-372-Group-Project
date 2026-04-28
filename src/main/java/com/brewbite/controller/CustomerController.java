package com.brewbite.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.*;
import com.brewbite.observer.BaristaObserver;
import com.brewbite.observer.ManagerObserver;
import com.brewbite.service.PersistenceService;
import com.brewbite.system.SystemState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CustomerController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<String> orderListView;

    private Order currentOrder = new Order();
    private OrderQueue orderQueue = SystemState.getOrderQueue();
    private Inventory inventory = SystemState.getInventory();
    private PersistenceService persistenceService = new PersistenceService();

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

        System.out.println("Loaded inventory data: " + persistenceService.loadInventorySnapshot());
        System.out.println("Loaded order data: " + persistenceService.loadOrderSnapshot());
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
        persistenceService.saveInventorySnapshot(
            "{ \"CoffeeBeans\": " + (inventory.isAvailable("CoffeeBeans", 1) ? "available" : "low") +
            ", \"Flour\": " + (inventory.isAvailable("Flour", 1) ? "available" : "low") + " }"
        );
                
        OrderItem orderItem = new OrderItem(item, 1);
        currentOrder.addItem(orderItem);

        orderListView.getItems().add(selected + " - $" + item.getPrice());
    }

    @FXML
    public void handlePlaceOrder() {

        double total = currentOrder.calculateTotal();

        System.out.printf("Order sent to queue. Total: $%.2f%n", total);

        System.out.printf("Revenue gained: $%.2f%n", total);

        orderQueue.addOrder(currentOrder);
        persistenceService.saveOrderSnapshot(
            "{ \"total\": " + total + ", \"status\": \"Queued\" }"
        );

        orderListView.getItems().clear();
        currentOrder = new Order();
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/role-selection.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
