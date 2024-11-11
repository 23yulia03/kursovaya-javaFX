package com.example.gamefx2;

public abstract class SudokuGame {

    protected int[][] solution;
    protected int[][] board;

    public abstract void initializeGame();

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolution() {
        return solution;
    }

    protected boolean checkSolution(int[][] userBoard) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (userBoard[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void showResult(boolean isCorrect) {
        String message = isCorrect ? "Поздравляем! Вы решили судоку!" : "Упс! Попробуйте еще раз.";
        System.out.println(message);
    }
}
