package com.brewbite.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class RoleController {

    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Scene scene = new Scene(loader.load(), 600, 400);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openCustomer(ActionEvent event) {
        switchScene(event, "customer-view.fxml");
    }

    @FXML
    public void openBarista(ActionEvent event) {
        switchScene(event, "barista-view.fxml");
    }

    @FXML
    public void openManager(ActionEvent event) {
        switchScene(event, "manager-view.fxml");
    }
}
