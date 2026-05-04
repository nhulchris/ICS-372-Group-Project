package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.model.Order;
import com.brewbite.model.OrderStatus;
import com.brewbite.service.OrderObserver;
import com.brewbite.util.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class BaristaController implements OrderObserver {

    @FXML
    private ListView<Order> orderListView;

    private final ObservableList<Order> observableOrders =
            FXCollections.observableArrayList();

    private final CafeSystem cafeSystem = CafeSystem.getInstance();

    @FXML
    public void initialize() {

        cafeSystem.getOrderManager().addObserver(this);

        orderListView.setItems(observableOrders);
    }

    @Override
    public void updateOrders(List<Order> orders) {
        System.out.println("Barista received order update: " + orders.size());
        observableOrders.setAll(orders);
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/brewbite/view/role-selection.fxml");
    }

    @FXML
    private void handleStartOrder() {

        Order selected = orderListView.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        cafeSystem.updateOrderStatus(
                selected.getOrderId(),
                OrderStatus.IN_PROGRESS
        );
    }

    @FXML
    private void handleCompleteOrder() {

        Order selected = orderListView.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        cafeSystem.updateOrderStatus(
                selected.getOrderId(),
                OrderStatus.COMPLETED
        );
    }
}