package com.example.gamefx2;

import java.util.Random;

public class ClassicSudoku extends SudokuGame {

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

        for (int i = 0; i < 40; i++) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            board[row][col] = 0;
        }

        return board;
    }
}