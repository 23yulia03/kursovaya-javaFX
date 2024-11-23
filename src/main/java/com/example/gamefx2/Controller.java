package com.example.gamefx2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Controller {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button checkButton;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label rulesLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label levelLabel;

    private TextField[][] cells;
    private ClassicSudoku game;

    private int secondsElapsed = 0; // Секунды, прошедшие с начала игры
    private Timeline timer; // Таймер

    private int highlightedValue = -1; // Хранение текущей подсвеченной цифры (по умолчанию нет подсветки)

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

            // Установить новый уровень сложности
            game.setDifficulty(difficulty);

            // Перезапустить игру
            game.initializeGame();
            initializeGrid(); // Перерисовываем поле

            // Обновить метку уровня
            levelLabel.setText("Уровень: " + selectedDifficulty);

            // Перезапустить таймер
            resetAndStartTimer();
        });
    }

    private void resetAndStartTimer() {
        if (timer != null) {
            timer.stop(); // Останавливаем текущий таймер
        }

        secondsElapsed = 0; // Сбрасываем счетчик времени
        timerLabel.setText("Время: 00:00"); // Обнуляем отображение таймера

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60; // Вычисляем минуты
            int seconds = secondsElapsed % 60; // Вычисляем секунды
            timerLabel.setText(String.format("Время: %02d:%02d", minutes, seconds)); // Отображаем в формате MM:SS
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Таймер бесконечный
        timer.play(); // Запуск таймера
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

                // Черные границы для квадратов 3x3
                if (row % 3 == 0) baseStyle += " -fx-border-top-width: 2px;";
                if (col % 3 == 0) baseStyle += " -fx-border-left-width: 2px;";
                if (row == 8) baseStyle += " -fx-border-bottom-width: 2px;";
                if (col == 8) baseStyle += " -fx-border-right-width: 2px;";

                textField.setStyle(baseStyle);

                // Обработчик клика на ячейку
                textField.setOnMouseClicked(event -> {
                    int value = (textField.getText().isEmpty()) ? 0 : Integer.parseInt(textField.getText());
                    if (highlightedValue != value) {
                        highlightSameValue(value);
                    }
                });

                cells[row][col] = textField;
                gridPane.add(textField, col, row);
            }
        }
    }

    private void highlightSameValue(int value) {
        if (highlightedValue == value) return;
        removeHighlight();

        // Подсветить все ячейки с новой цифрой
        String highlightStyle = "-fx-background-color: #add8e6;"; // Нежно-голубой фон для выделения

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = cells[row][col];
                if (!cell.getText().isEmpty() && Integer.parseInt(cell.getText()) == value) {
                    cell.setStyle(cell.getStyle() + highlightStyle);
                }
            }
        }

        highlightedValue = value;
    }

    private void removeHighlight() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = cells[row][col];
                String currentStyle = cell.getStyle();
                if (currentStyle.contains("-fx-background-color: #add8e6;")) {
                    cell.setStyle(currentStyle.replace("-fx-background-color: #add8e6;", ""));
                }
            }
        }

        highlightedValue = -1;
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

        if (isCorrect) {
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            alert.setContentText("Поздравляем! Вы решили судоку за " + String.format("%02d:%02d", minutes, seconds));
        } else {
            alert.setContentText("Упс! Попробуйте еще раз.");
        }

        alert.showAndWait();
    }
}
