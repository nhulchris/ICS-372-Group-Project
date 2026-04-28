package com.brewbite.controller;

import com.brewbite.factory.MenuItemFactory;
import com.brewbite.model.MenuItem;
import com.brewbite.model.Order;
import com.brewbite.model.OrderItem;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CustomerController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<String> orderListView;

    private Order currentOrder = new Order();

    @FXML
    public void initialize() {
        // Sample menu items (will later come from JSON)
        menuListView.setItems(FXCollections.observableArrayList(
                "Latte",
                "Espresso",
                "Croissant",
                "Muffin"
        ));
    }

    @FXML
    public void handleAddToOrder() {
        String selected = menuListView.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        MenuItem item;

        // Simple type detection (temporary)
        if (selected.equalsIgnoreCase("Latte") || selected.equalsIgnoreCase("Espresso")) {
            item = MenuItemFactory.createItem("beverage", selected, 3.0);
        } else {
            item = MenuItemFactory.createItem("pastry", selected, 2.0);
        }

        OrderItem orderItem = new OrderItem(item, 1);
        currentOrder.addItem(orderItem);

        orderListView.getItems().add(selected + " - $" + item.getPrice());
    }

    @FXML
    public void handlePlaceOrder() {
        double total = currentOrder.calculateTotal();

        System.out.println("Order placed. Total: $" + total);

        orderListView.getItems().clear();
        currentOrder = new Order();
    }
}
