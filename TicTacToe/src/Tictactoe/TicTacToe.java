package Tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private static final long serialVersionUID = 1L;

    // Private variables
    private Board board = new Board();
    private GameBoardPanel boardPanel = new GameBoardPanel(board);
    private JButton btnNewGame = new JButton("New Game");
    private State currentState;
    private Seed currentPlayer;  // Use Seed instead of Player
    private JLabel statusBar;  // Label for the status bar
    private static final Font FONT_STATUS = new Font("Arial", Font.PLAIN, 16);  // Set a font for the status bar
    private static final Color COLOR_BG_STATUS = new Color(220, 220, 220);  // Background color for the status bar

    // Constructor
    public TicTacToe() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Add the game board to the center
        cp.add(boardPanel, BorderLayout.CENTER);

        // Add the "New Game" button to the top
        cp.add(btnNewGame, BorderLayout.NORTH);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();  // Start a new game
            }
        });

        // Create and configure the status bar
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        // Add the status bar to the bottom of the frame
        cp.add(statusBar, BorderLayout.SOUTH);

        // Initialize the game board to start the game
        startNewGame();

        pack();  // Pack the UI components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window-closing
        setTitle("Tic-Tac-Toe");  // Update window title
        setVisible(true);
    }

    // Method to start a new game
    private void startNewGame() {
        board.newGame();  // Reset the board
        currentPlayer = Seed.CROSS;  // Cross always starts
        currentState = State.PLAYING;  // Game is ongoing
        updateStatusBar();  // Update the status bar
        boardPanel.repaint();  // Redraw the board
        playSound(SoundEffect.EXPLODE);  // Play sound on new game start
    }

    // Method to handle a player making a move
    public void playerMove(int row, int col) {
        if (board.cells[row][col].content == Seed.NO_SEED && currentState == State.PLAYING) {
            // Make the move
            currentState = board.stepGame(currentPlayer, row, col);

            // Refresh the display
            boardPanel.repaint();

            // Update the status bar based on the game state
            updateStatusBar();

            // Switch currentPlayer if the game is still ongoing
            if (currentState == State.PLAYING) {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            }
        }
    }

    // Method to update the status bar based on the current state
    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again");
        }
    }

    // Method to play sound effect (stub)
    private void playSound(SoundEffect soundEffect) {
        soundEffect.play();
    }

    // Main entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);  // Launch the game in the EDT
    }
}
