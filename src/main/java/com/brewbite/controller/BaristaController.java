package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.model.Order;
import com.brewbite.model.OrderStatus;
import com.brewbite.service.OrderObserver;
import com.brewbite.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Barista dashboard.
 *
 * Shows pending/in-progress/ready orders in FIFO order, plus a separate
 * panel listing completed/cancelled orders. Implements OrderObserver so
 * the lists update automatically as customers place new orders elsewhere
 * in the application.
 */
public class BaristaController implements OrderObserver {

    @FXML private ListView<Order> orderListView;
    @FXML private ListView<Order> historyListView;
    @FXML private Label statusLabel;

    private final ObservableList<Order> activeOrders = FXCollections.observableArrayList();
    private final ObservableList<Order> historyOrders = FXCollections.observableArrayList();

    private final CafeSystem cafeSystem = CafeSystem.getInstance();

    @FXML
    public void initialize() {
        orderListView.setItems(activeOrders);
        historyListView.setItems(historyOrders);

        cafeSystem.getOrderManager().addObserver(this);
        // Seed the lists with the current state.
        updateOrders(cafeSystem.getOrderManager().getOrders());
    }

    /**
     * Splits incoming orders into active (workable) vs history (terminal).
     */
    @Override
    public void updateOrders(List<Order> orders) {
        List<Order> active = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING
                          || o.getStatus() == OrderStatus.IN_PROGRESS
                          || o.getStatus() == OrderStatus.READY_FOR_PICKUP)
                .collect(Collectors.toList());

        List<Order> history = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED
                          || o.getStatus() == OrderStatus.CANCELLED)
                .collect(Collectors.toList());

        activeOrders.setAll(active);
        historyOrders.setAll(history);
    }

    @FXML
    private void handleStartOrder() {
        Order selected = orderListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an order first.");
            return;
        }
        if (selected.getStatus() != OrderStatus.PENDING) {
            statusLabel.setText("Order is already started.");
            return;
        }
        cafeSystem.updateOrderStatus(selected.getOrderId(), OrderStatus.IN_PROGRESS);
        statusLabel.setText("Order #" + selected.getOrderId() + " started.");
    }

    @FXML
    private void handleMarkReady() {
        Order selected = orderListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an order first.");
            return;
        }
        if (selected.getStatus() != OrderStatus.IN_PROGRESS) {
            statusLabel.setText("Only in-progress orders can be marked ready.");
            return;
        }
        cafeSystem.updateOrderStatus(selected.getOrderId(), OrderStatus.READY_FOR_PICKUP);
        statusLabel.setText("Order #" + selected.getOrderId() + " is ready for pickup.");
    }

    @FXML
    private void handleCompleteOrder() {
        Order selected = orderListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an order first.");
            return;
        }
        cafeSystem.updateOrderStatus(selected.getOrderId(), OrderStatus.COMPLETED);
        statusLabel.setText("Order #" + selected.getOrderId() + " completed.");
    }

    @FXML
    private void handleCancelOrder() {
        Order selected = orderListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an order first.");
            return;
        }
        cafeSystem.updateOrderStatus(selected.getOrderId(), OrderStatus.CANCELLED);
        statusLabel.setText("Order #" + selected.getOrderId() + " cancelled.");
    }

    @FXML
    private void handleBack() {
        cafeSystem.getOrderManager().removeObserver(this);
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }
}
