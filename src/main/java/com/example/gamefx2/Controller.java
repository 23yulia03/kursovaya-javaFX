package com.example.gamefx2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML
    private VBox rootVBox;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button checkButton;

    @FXML
    private Label rulesLabel;

    private TextField[][] cells;
    private SudokuGame game;

    public void initializeGame() {
        game = new ClassicSudoku();
        game.initializeGame();
        initializeGrid();
        initializeRules();
    }

    private void initializeGrid() {
        cells = new TextField[9][9];
        int[][] board = game.getBoard();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField textField = new TextField();
                textField.setPrefSize(50, 50);
                textField.setStyle("-fx-font-size: 20px;");
                textField.setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                textField.setEditable(board[row][col] == 0);

                // Выделение квадратов 3x3
                if ((row / 3 + col / 3) % 2 == 0) {
                    textField.setStyle("-fx-font-size: 20px; -fx-background-color: #f0f0f0;");
                } else {
                    textField.setStyle("-fx-font-size: 20px; -fx-background-color: #ffffff;");
                }

                // Выделение цифр по умолчанию
                if (board[row][col] != 0) {
                    textField.setStyle("-fx-font-size: 20px; -fx-background-color: #d0d0d0; -fx-text-fill: #000000;");
                }

                cells[row][col] = textField;
                gridPane.add(textField, col, row);
            }
        }
    }

    private void initializeRules() {
        rulesLabel.setText("Правила игры:\n" +
                "1. В каждой строке должны быть числа от 1 до 9 без повторений.\n" +
                "2. В каждом столбце должны быть числа от 1 до 9 без повторений.\n" +
                "3. В каждом квадрате 3x3 должны быть числа от 1 до 9 без повторений.");
    }

    @FXML
    private void checkSolution() {
        int[][] userBoard = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = cells[row][col].getText();
                userBoard[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }

        boolean isCorrect = game.checkSolution(userBoard);
        showAlert(isCorrect);
    }

    private void showAlert(boolean isCorrect) {
        Alert alert = new Alert(isCorrect ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(isCorrect ? "Победа!" : "Проигрыш!");
        alert.setHeaderText(null);
        alert.setContentText(isCorrect ? "Поздравляем! Вы решили судоку!" : "Упс! Попробуйте еще раз.");
        alert.showAndWait();
    }
}