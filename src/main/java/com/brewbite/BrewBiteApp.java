package com.brewbite;

import com.brewbite.facade.CafeSystem;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import com.brewbite.util.SceneManager;

public class BrewBiteApp extends Application {

    private static CafeSystem cafeSystem;

    @Override
    public void start(Stage primaryStage) {
        System.out.println(SceneManager.class.getResource("/com/brewbite/view/barista.fxml"));
        try {

            SceneManager.setStage(primaryStage);

            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/brewbite/view/role-selection.fxml")
            );

            primaryStage.setTitle("Brew & Bite");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CafeSystem getCafeSystem() {
        return cafeSystem;
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