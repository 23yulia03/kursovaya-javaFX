package com.example.gamefx2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label rulesLabel;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        initializeDifficultyComboBox();
        initializeRules();
    }

    private void initializeDifficultyComboBox() {
        difficultyComboBox.getItems().addAll("Легкий", "Средний", "Сложный");
        difficultyComboBox.setValue("Легкий"); // Уровень по умолчанию
    }

    private void initializeRules() {
        rulesLabel.setText("В классическом судоку игровое поле – это 9 блоков размером 3х3, \n" +
                "в которые необходимо вписывать значения от 1 до 9. \n" +
                "\n" +
                "Основные правила судоку такие:\n" +
                "1) числа по вертикали не должны повторяться;\n" +
                "2) числа по горизонтали не должны дублироваться;\n" +
                "3) число может встречаться в квадрате 3х3 только 1 раз.");
    }

    @FXML
    private void startGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Начать игру");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите начать игру?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
                    Parent root = loader.load();
                    GameController gameController = loader.getController();
                    gameController.setPrimaryStage(primaryStage);
                    gameController.setDifficulty(difficultyComboBox.getValue());
                    gameController.initializeGame();

                    primaryStage.setScene(new Scene(root, 630, 800));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}