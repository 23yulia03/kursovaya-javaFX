package com.example.gamefx2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

public class GameController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button checkButton;

    @FXML
    private Label timerLabel;

    @FXML
    private Label levelLabel;

    private TextField[][] cells;
    private ClassicSudoku game;

    private int secondsElapsed = 0; // Секунды, прошедшие с начала игры
    private Timeline timer; // Таймер

    private int highlightedValue = -1; // Хранение текущей подсвеченной цифры (по умолчанию нет подсветки)

    private Stage primaryStage;
    private String difficulty;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void initializeGame() {
        game = new ClassicSudoku();
        Difficulty gameDifficulty = switch (difficulty) {
            case "Легкий" -> Difficulty.EASY;
            case "Средний" -> Difficulty.MEDIUM;
            case "Сложный" -> Difficulty.HARD;
            default -> Difficulty.EASY;
        };
        game.setDifficulty(gameDifficulty);
        game.initializeGame();
        initializeGrid();
        levelLabel.setText("Уровень: " + difficulty);
        resetAndStartTimer();
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
                textField.setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                textField.setEditable(board[row][col] == 0);

                // Формируем стиль для ячейки
                StringBuilder style = new StringBuilder("-fx-font-size: 20px; -fx-alignment: center; -fx-border-color: black;");

                // Добавляем тонкие внутренние границы
                style.append(" -fx-border-width: 1px;");

                // Выделяем жирные границы для квадратов 3x3
                if (row % 3 == 0) style.append(" -fx-border-top-width: 3px;"); // Жирная верхняя граница
                if (col % 3 == 0) style.append(" -fx-border-left-width: 3px;"); // Жирная левая граница
                if (row == 8) style.append(" -fx-border-bottom-width: 3px;"); // Жирная нижняя граница
                if (col == 8) style.append(" -fx-border-right-width: 3px;"); // Жирная правая граница

                // Цвет фона для удобства чтения
                if ((row / 3 + col / 3) % 2 == 0) {
                    style.append(" -fx-background-color: #f9f9f9;"); // Светлый фон
                } else {
                    style.append(" -fx-background-color: #ffffff;"); // Белый фон
                }

                // Если ячейка фиксированная (начальные числа)
                if (board[row][col] != 0) {
                    style.append(" -fx-background-color: #dcdcdc; -fx-text-fill: black;"); // Фиксированные числа
                }

                textField.setStyle(style.toString());

                // Обработчик клика для подсветки одинаковых чисел
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

        if (isCorrect) {
            showRestartOrMainMenuAlert();
        } else {
            showRestartAlert();
        }
    }

    private void showRestartOrMainMenuAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Победа!");
        alert.setHeaderText(null);
        alert.setContentText("Вы хотите решить еще раз или вернуться в главное меню?");

        ButtonType restartButton = new ButtonType("Решить еще раз");
        ButtonType mainMenuButton = new ButtonType("Вернуться в главное меню");
        alert.getButtonTypes().setAll(restartButton, mainMenuButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == restartButton) {
                initializeGame();
            } else if (result.get() == mainMenuButton) {
                goToMainMenu();
            }
        }
    }

    private void showRestartAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Проигрыш!");
        alert.setHeaderText(null);
        alert.setContentText("Вы хотите начать сначала?");

        ButtonType restartButton = new ButtonType("Начать сначала");
        ButtonType mainMenuButton = new ButtonType("Вернуться в главное меню");
        alert.getButtonTypes().setAll(restartButton, mainMenuButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == restartButton) {
                initializeGame();
            } else if (result.get() == mainMenuButton) {
                goToMainMenu();
            }
        }
    }

    private void goToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Parent root = loader.load();
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setPrimaryStage(primaryStage);

            primaryStage.setScene(new Scene(root, 630, 800));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}