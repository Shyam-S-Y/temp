package com.idcard.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/StudentForm.fxml"));
        primaryStage.setTitle("Student ID Generator");

        // Set Application Icon
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        } catch (Exception e) {
            System.out.println("Could not load logo.png");
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
