package com.example.gamefx2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Судоку");
        primaryStage.setScene(new Scene(root, 630, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}