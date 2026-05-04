package com.brewbite.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath) {

        
        try {
            URL resource = SceneManager.class.getResource(fxmlPath);
            if (resource == null) {
                throw new IllegalStateException(
                        "FXML not found: " + fxmlPath +
                        "\nCheck file location under src/main/resources"
                );
            }

            Parent root = FXMLLoader.load(resource);
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}