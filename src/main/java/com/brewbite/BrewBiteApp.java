package com.brewbite;

import com.brewbite.facade.CafeSystem;
import com.brewbite.util.SceneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Brew & Bite Cafe System.
 * Bootstraps the CafeSystem facade, loads the role-selection scene,
 * and ensures all data is persisted on application shutdown.
 */
public class BrewBiteApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the facade up front so menu, inventory, and saved
            // orders are loaded before any scene tries to read from them.
            CafeSystem.getInstance();

            SceneManager.setStage(primaryStage);

            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/brewbite/view/role-selection.fxml")
            );

            primaryStage.setTitle("Brew & Bite");
            primaryStage.setScene(new Scene(root, 400, 320));
            primaryStage.setOnCloseRequest(e -> CafeSystem.getInstance().saveData());
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.out.println("Saving data...");
        CafeSystem.getInstance().saveData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
