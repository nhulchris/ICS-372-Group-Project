package com.brewbite;

import javafx.application.Application;
import javafx.stage.Stage;

public class BrewBiteApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Brew & Bite Cafe System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
