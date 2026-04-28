package com.brewbite.controller;

import com.brewbite.model.OrderItem;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import com.brewbite.model.Order;
import com.brewbite.model.OrderQueue;
import com.brewbite.system.SystemState;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class BaristaController {

    @FXML
    public void initialize() {
        int count = 1;
    
        for (Order order : orderQueue.getOrders()) {
            StringBuilder orderText = new StringBuilder("Order #" + count + ": ");
    
        for (int i = 0; i < order.getItems().size(); i++) {
            OrderItem item = order.getItems().get(i);
            orderText.append(item.getItem().getName());
        
            if (i < order.getItems().size() - 1) {
                orderText.append(", ");
            }
        }
            
            orderQueueListView.getItems().add(orderText.toString());
            count++;
        }
    }

    @FXML
    private ListView<String> orderQueueListView;

    private OrderQueue orderQueue = SystemState.getOrderQueue();

    public void setOrderQueue(OrderQueue queue) {
        this.orderQueue = queue;
    }
    
    @FXML
    public void handleCompleteOrder() {
        if (orderQueue == null) return;
    
        Order order = orderQueue.getNextOrder();
    
        if (order != null) {
    
            if (!orderQueueListView.getItems().isEmpty()) {
                orderQueueListView.getItems().remove(0);
            }
    
            System.out.println("Order completed.");
        }
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

    public void addOrderToView(String orderText) {
        orderQueueListView.getItems().add(orderText);
    }
}
