package Sudoku; /**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231083 - Arya Wiraguna Dwiputra
 * 2 - 5026231097 - Gusti Gratia Delpiera
 * 3 - 5026231152 - M. Naufal Erwin Effendi
 */

import java.util.*;

public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be used
    //  to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        resetPuzzle();
        generateSolvedPuzzle();
        setCellsToGuess(cellsToGuess);
    }

    private void resetPuzzle() {
        // Reset all numbers to 0
        for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
            for (int j = 0; j < SudokuConstants.GRID_SIZE; j++) {
                this.numbers[i][j] = 0;
                this.isGiven[i][j] = true;
            }
        }
    }

    private void generateSolvedPuzzle() {
        fillGrid(numbers);
    }

    private boolean fillGrid(int[][] grid) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                if (grid[row][col] == 0) {
                    List<Integer> numbersList = new ArrayList<>();
                    for (int num = 1; num <= SudokuConstants.GRID_SIZE; num++) {
                        numbersList.add(num);
                    }
                    Collections.shuffle(numbersList);

                    for (int num : numbersList) {
                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid(grid)) {
                                return true;
                            }
                            grid[row][col] = 0;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }


    private boolean isSafe(int[][] grid, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < SudokuConstants.GRID_SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }

        // Check the subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private void setCellsToGuess(int cellsToGuess) {
        // Initialize isGiven array to all true (indicating all cells are given)
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                isGiven[row][col] = true;
            }
        }

        // Randomly set some cells as 'not given'
        int cellsRemoved = 0;
        while (cellsRemoved < cellsToGuess) {
            int row = (int) (Math.random()*9);
            int col = (int) (Math.random()*9);

            if (isGiven[row][col]) {  // If it's still marked as 'given', remove it
                isGiven[row][col] = false;
                cellsRemoved++;
            }
        }
    }
    //(For advanced students) use singleton design pattern for this class
}
