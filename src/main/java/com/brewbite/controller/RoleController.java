package com.brewbite.controller;

import com.brewbite.util.SceneManager;
import javafx.fxml.FXML;

public class RoleSelectionController {

    @FXML
    private void handleCustomer() {
        SceneManager.switchScene("/com/brewbite/view/customer-name.fxml");
    }

    @FXML
    private void handleBarista() {
        SceneManager.switchScene("/com/brewbite/view/login.fxml");
    }

    @FXML
    private void handleManager() {
        SceneManager.switchScene("/com/brewbite/view/login.fxml");
    }

    
}