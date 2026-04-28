import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

package com.brewbite;

import javafx.application.Application;
import javafx.stage.Stage;

public class BrewBiteApp extends Application {

@Override
public void start(Stage primaryStage) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);

        primaryStage.setTitle("Brew & Bite Cafe System");
        primaryStage.setScene(scene);
        primaryStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
