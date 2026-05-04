package com.brewbite.controller;

import com.brewbite.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {

        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals("barista") && pass.equals("123")) {
            SceneManager.switchScene("/com/brewbite/view/barista.fxml");
        }
        else if (user.equals("manager") && pass.equals("123")) {
            SceneManager.switchScene("/com/brewbite/view/manager.fxml");
        }
        else {
            statusLabel.setText("Invalid credentials");
        }
    }
}