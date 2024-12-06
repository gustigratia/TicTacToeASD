/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231083 - Arya Wiraguna Dwiputra
 * 2 - 5026231097 - Gusti Gratia Delpiera
 * 3 - 5026231152 - M. Naufal Erwin Effendi
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int BOARD_WIDTH  = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle = new Puzzle();

    // Timer
    private JLabel timerLabel;
    private Timer timer;
    private int elapsedTime;
    private boolean isPaused = false;
    private JButton btnPauseResume;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new BorderLayout());  // JPanel

        JPanel boardPanel = new JPanel(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                boardPanel.add(cells[row][col]);   // JPanel
            }
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        //  Cells (JTextFields)
        // .........
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        // .........

        for (int row = 0;row<SudokuConstants.GRID_SIZE;row++) {
            for (int col = 0;col<SudokuConstants.GRID_SIZE;col++) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);   // For all editable rows and cols
                }
            }
        }



        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + CELL_SIZE));

        timerLabel = new JLabel("Time: 00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        btnPauseResume = new JButton("Pause");
        btnPauseResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                toggleTimer();  // Toggle between pause and resume
            }
        });

        bottomPanel.add(timerLabel, BorderLayout.CENTER);
        bottomPanel.add(btnPauseResume, BorderLayout.EAST);

        super.add(boardPanel, BorderLayout.CENTER);
        super.add(bottomPanel, BorderLayout.SOUTH);

        elapsedTime = 0;  // Start with 0 seconds
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {  // Only update time if not paused
                    elapsedTime++;
                    updateTimerLabel();
                }
            }
        });
    }

    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        String time = String.format("Time: %02d:%02d", minutes, seconds);
        timerLabel.setText(time);
    }

    private void toggleTimer() {
        if (isPaused) {
            // Resume the timer
            timer.start();
            btnPauseResume.setText("Pause");
        } else {
            // Pause the timer
            timer.stop();
            btnPauseResume.setText("Resume");
        }
        isPaused = !isPaused;
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.stop();
        }
    }


    public void resumeTimer() {
        if (timer != null && !isPaused) {
            timer.start();
        }
    }
    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     */
    public void newGame(String difficulty) {
        // Generate a new puzzle
        int cellsToGuess;
        switch (difficulty) {
            case "Easy":
                cellsToGuess = 15;
                break;
            case "Medium":
                cellsToGuess = 25;
                break;
            case "Hard":
                cellsToGuess = 35;
                break;
            default:
                cellsToGuess = 20;
                break;
        }
        puzzle.newPuzzle(cellsToGuess);
        elapsedTime = 0;
        updateTimerLabel();
        updateTimerLabel();
        isPaused = false;
        timer.start();

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    // .........
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell)e.getSource();

            // Retrieve the int entered
            int numberIn = Integer.parseInt(sourceCell.getText());
            // For debugging
            System.out.println("You entered " + numberIn);

            /*
             * [TODO 5] (later - after TODO 3 and 4)
             * Check the numberIn against sourceCell.number.
             * Update the cell status sourceCell.status,
             * and re-paint the cell via sourceCell.paint().
             */
            if (numberIn == sourceCell.number) {
                sourceCell.status = CellStatus.CORRECT_GUESS;
            } else {
                sourceCell.status = CellStatus.WRONG_GUESS;
            }
            sourceCell.paint();   // re-paint this cell based on its status

            /*
             * [TODO 6] (later)
             * Check if the player has solved the puzzle after this move,
             *   by calling isSolved(). Put up a congratulation JOptionPane, if so.
             */
            if(isSolved()){
                JOptionPane.showMessageDialog(null, "Congratulation!");
                SoundEffect.WIN.play();
            }
        }
    }
}
