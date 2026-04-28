package com.brewbite.controller;

import com.brewbite.model.Order;
import com.brewbite.model.OrderQueue;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class BaristaController {

    @FXML
    private ListView<String> orderQueueListView;

    private OrderQueue orderQueue;

    public void setOrderQueue(OrderQueue queue) {
        this.orderQueue = queue;
    }

    @FXML
    public void handleCompleteOrder() {
        if (orderQueue == null) return;

        Order order = orderQueue.getNextOrder();

        if (order != null) {
            orderQueueListView.getItems().remove(0);
            System.out.println("Order completed.");
        }
    }

    public void addOrderToView(String orderText) {
        orderQueueListView.getItems().add(orderText);
    }
}
