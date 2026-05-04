package com.brewbite.controller;

import com.brewbite.facade.CafeSystem;
import com.brewbite.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomerNameController {

    @FXML private TextField nameField;
    @FXML private Label statusLabel;

    @FXML
    private void handleContinue() {
        String name = nameField.getText();

        if (name == null || name.isEmpty()) {
            statusLabel.setText("Enter a name.");
            return;
        }

        CafeSystem.getInstance().setCurrentCustomerName(name);

        SceneManager.switchScene("/com/brewbite/view/customer-order.fxml");
    }
}