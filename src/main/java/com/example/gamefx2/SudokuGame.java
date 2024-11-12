package com.example.gamefx2;

public abstract class SudokuGame implements SudokuGameInterface {

    protected int[][] solution;
    protected int[][] board;

    @Override
    public abstract void initializeGame();

    @Override
    public int[][] getBoard() {
        return board;
    }

    @Override
    public int[][] getSolution() {
        return solution;
    }

    @Override
    public boolean checkSolution(int[][] userBoard) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (userBoard[row][col] != solution[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
}
