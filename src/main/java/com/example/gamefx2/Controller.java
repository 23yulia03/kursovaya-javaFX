package com.example.gamefx2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label rulesLabel;

    private TextField[][] cells;
    private ClassicSudoku game;

    public void initializeGame() {
        game = new ClassicSudoku();
        initializeDifficultyComboBox();
        game.initializeGame();
        initializeGrid();
        initializeRules();
    }

    private void initializeDifficultyComboBox() {
        difficultyComboBox.getItems().addAll("Легкий", "Средний", "Сложный");
        difficultyComboBox.setValue("Средний"); // Уровень по умолчанию

        difficultyComboBox.setOnAction(event -> {
            String selectedDifficulty = difficultyComboBox.getValue();
            Difficulty difficulty = switch (selectedDifficulty) {
                case "Легкий" -> Difficulty.EASY;
                case "Средний" -> Difficulty.MEDIUM;
                case "Сложный" -> Difficulty.HARD;
                default -> Difficulty.MEDIUM;
            };
            game.setDifficulty(difficulty);
            game.initializeGame();
            initializeGrid(); // Перерисовываем поле
        });
    }

    private void initializeGrid() {
        gridPane.getChildren().clear();
        cells = new TextField[9][9];
        int[][] board = game.getBoard();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField textField = new TextField();
                textField.setPrefSize(50, 50);
                textField.setStyle("-fx-font-size: 20px;");
                textField.setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                textField.setEditable(board[row][col] == 0);

                // Стиль для ячейки
                String baseStyle = "-fx-font-size: 20px; -fx-border-color: black; -fx-border-width: 0.5px;";

                // Чередующийся фон блоков 3x3
                if ((row / 3 + col / 3) % 2 == 0) {
                    baseStyle += "-fx-background-color: #f0f0f0;";
                } else {
                    baseStyle += "-fx-background-color: #ffffff;";
                }

                // Для начальных чисел (нередактируемых)
                if (board[row][col] != 0) {
                    baseStyle += "-fx-background-color: #d0d0d0; -fx-text-fill: #000000;";
                }

                // Жирные границы для квадратов 3x3
                if (row % 3 == 0) baseStyle += " -fx-border-top-width: 2px;";
                if (col % 3 == 0) baseStyle += " -fx-border-left-width: 2px;";
                if (row == 8) baseStyle += " -fx-border-bottom-width: 2px;";
                if (col == 8) baseStyle += " -fx-border-right-width: 2px;";

                textField.setStyle(baseStyle);

                // Добавляем обработчик фокуса
                final int blockRow = row / 3;
                final int blockCol = col / 3;

                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        highlightBlock(blockRow, blockCol, true);  // Выделяем блок
                    } else {
                        highlightBlock(blockRow, blockCol, false); // Убираем выделение
                    }
                });

                cells[row][col] = textField;
                gridPane.add(textField, col, row);
            }
        }
    }

    private void highlightBlock(int blockRow, int blockCol, boolean highlight) {
        String highlightStyle = "-fx-background-color: #c0e0ff;"; // Голубой фон для выделенного блока

        for (int row = blockRow * 3; row < blockRow * 3 + 3; row++) {
            for (int col = blockCol * 3; col < blockCol * 3 + 3; col++) {
                TextField cell = cells[row][col];
                if (highlight) {
                    cell.setStyle(cell.getStyle() + highlightStyle);
                } else {
                    // Убираем только выделение, оставляя другие стили
                    cell.setStyle(cell.getStyle().replace(highlightStyle, ""));
                }
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
