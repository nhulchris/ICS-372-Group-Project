package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.model.MenuItem;
import com.brewbite.model.Order;
import com.brewbite.model.OrderItem;
import com.brewbite.service.MenuObserver;
import com.brewbite.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerOrderController implements MenuObserver {

    @FXML
    private ListView<MenuItem> menuListView;
    
    @FXML
    private ListView<OrderItem> cartListView;

    @FXML
    private TextField quantityField;

    @FXML
    private Label totalLabel;

    @FXML
    private Label statusLabel;

    private final CafeSystem cafeSystem = CafeSystem.getInstance();
    private final ObservableList<OrderItem> cartItems = FXCollections.observableArrayList();
    private Order currentOrder;

    @FXML
    public void initialize() {

        String customerName = cafeSystem.getCurrentCustomerName();

        if (customerName == null || customerName.isBlank()) {
            customerName = "Customer";
        }

        currentOrder = cafeSystem.createOrder(customerName);

        cartListView.setItems(cartItems);

        cafeSystem.getMenuManager().addObserver(this);
        updateMenu(cafeSystem.getMenuItems());

        updateTotal();
        statusLabel.setText("");
    }


    @FXML
    private void handleAddToCart() {

        MenuItem selected = menuListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select an item.");
            return;
        }

        int qty;

        try {
            qty = Integer.parseInt(quantityField.getText());

            if (qty <= 0) {
                statusLabel.setText("Quantity must be greater than 0.");
                return;
            }

        } catch (Exception e) {
            statusLabel.setText("Invalid quantity.");
            return;
        }

        OrderItem item = new OrderItem(selected, qty);

        cartItems.add(item);
        currentOrder.addItem(item);

        quantityField.clear();

        updateTotal();
        statusLabel.setText("Item added to cart.");
    }

    @FXML
    private void handlePlaceOrder() {

        if (currentOrder.getItems().isEmpty()) {
            statusLabel.setText("Cart is empty.");
            return;
        }

        boolean success = cafeSystem.placeOrder(currentOrder);

        if (success) {
            statusLabel.setText("Order placed successfully!");

            cartItems.clear();
            currentOrder = new Order("Customer");
            updateTotal();

        } else {
            statusLabel.setText("Not enough inventory to place order.");
        }
    }

    private void updateTotal() {
        totalLabel.setText(
                "Total: $" + String.format("%.2f", currentOrder.calculateTotal())
        );
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }

    @Override
    public void updateMenu(java.util.List<MenuItem> menu) {
        menuListView.setItems(
                javafx.collections.FXCollections.observableArrayList(menu)
        );
    }
}