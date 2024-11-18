package com.example.gamefx2;

import java.util.Random;

public class ClassicSudoku extends SudokuGame {

    private Difficulty difficulty = Difficulty.MEDIUM;

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void initializeGame() {
        solution = generateSolution();
        board = generateBoard(solution);
    }

    private int[][] generateSolution() {
        int[][] solution = new int[9][9];
        solveSudoku(solution, 0, 0);
        return solution;
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (row == 9) {
            return true;
        }
        if (col == 9) {
            return solveSudoku(board, row + 1, 0);
        }
        if (board[row][col] != 0) {
            return solveSudoku(board, row, col + 1);
        }

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board, row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[row / 3 * 3 + i / 3][col / 3 * 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private int[][] generateBoard(int[][] solution) {
        int[][] board = new int[9][9];
        Random random = new Random();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = solution[i][j];
            }
        }

        int cellsToRemove = switch (difficulty) {
            case EASY -> 30;   // 30 пустых клеток
            case MEDIUM -> 35; // 35 пустых клеток
            case HARD -> 45;   // 45 пустых клеток
        };

        for (int i = 0; i < cellsToRemove; i++) {
            int row, col;
            do {
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while (board[row][col] == 0);
            board[row][col] = 0;
        }

        return board;
    }
}
