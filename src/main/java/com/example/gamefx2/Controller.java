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
    private SudokuGameInterface game;

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

                String baseStyle = "-fx-font-size: 20px; -fx-border-color: black; -fx-border-width: 0.5px;";

                // Применение фона для блоков 3x3
                if ((row / 3 + col / 3) % 2 == 0) {
                    baseStyle += "-fx-background-color: #f0f0f0;";
                } else {
                    baseStyle += "-fx-background-color: #ffffff;";
                }

                // Задаём стиль для начальных (нередактируемых) чисел
                if (board[row][col] != 0) {
                    baseStyle += "-fx-background-color: #d0d0d0; -fx-text-fill: #000000;";
                }

                // Жирные границы для квадратов 3x3
                if (row % 3 == 0) baseStyle += " -fx-border-top-width: 2px;";
                if (col % 3 == 0) baseStyle += " -fx-border-left-width: 2px;";
                if (row == 8) baseStyle += " -fx-border-bottom-width: 2px;";
                if (col == 8) baseStyle += " -fx-border-right-width: 2px;";

                textField.setStyle(baseStyle);

                cells[row][col] = textField;
                gridPane.add(textField, col, row);
            }
        }
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
