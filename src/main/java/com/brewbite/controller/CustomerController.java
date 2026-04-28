package com.brewbite.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CustomerController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private ListView<String> orderListView;

    @FXML
    public void handleAddToOrder() {
        System.out.println("Add to order clicked");
    }

    @FXML
    public void handlePlaceOrder() {
        System.out.println("Order placed");
    }
}
