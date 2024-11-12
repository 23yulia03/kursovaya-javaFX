package com.example.gamefx2;

public interface SudokuGameInterface {
    void initializeGame();
    int[][] getBoard();
    int[][] getSolution();
    boolean checkSolution(int[][] userBoard);
}
