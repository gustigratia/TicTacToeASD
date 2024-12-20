/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231083 - Arya Wiraguna Dwiputra
 * 2 - 5026231097 - Gusti Gratia Delpiera
 * 3 - 5026231152 - M. Naufal Erwin Effendi
 */
package Othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OthelloMain extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the game board
    public static final int ROWS = 8;  // ROWS x COLS cells
    public static final int COLS = 8;

    // Define named constants for the drawing graphics
    public static final int CELL_SIZE = 75; // cell width/height (square)
    public static final int BOARD_WIDTH  = CELL_SIZE * COLS; // the drawing canvas
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 7;                  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    // Symbols (cross/black) are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
    public static final Color COLOR_BG = new Color(173, 216, 230);  // background
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_GRID   = new Color(0, 0, 0, 128);  // grid lines
    public static final Color COLOR_WHITE  = new Color(255, 255, 255);  // White
    public static final Color COLOR_BLACK  = new Color(0, 0, 0); // Black
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    public boolean isValidMove(Seed player, int row, int col) {
        if (board[row][col] != Seed.NO_SEED) {
            return false; // The cell must be empty
        }

        Seed opponent = (player == Seed.WHITE) ? Seed.BLACK : Seed.WHITE;

        // Directions for row/column/direction (8 directions)
        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};  // up-left, up, up-right, right, down-right, down, down-left, left
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};  // up-left, up, up-right, right, down-right, down, down-left, left

        // Check all 8 directions
        for (int i = 0; i < 8; i++) {
            int r = row + rowDirections[i];
            int c = col + colDirections[i];

            boolean canFlip = false;

            // Move in the current direction while the cell contains the opponent's piece
            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == opponent) {
                r += rowDirections[i];
                c += colDirections[i];
                canFlip = true;  // Found an opponent's piece, can potentially flip
            }

            // Check if the last cell is the player's seed
            if (canFlip && r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
                return true;  // Found a valid move in this direction
            }
        }

        return false;  // No valid direction found
    }

    // This enum (inner class) contains the various states of the game
    public enum State {
        PLAYING, DRAW, WHITE_WON, BLACK_WON
    }
    private State currentState;  // the current game state

    // This enum (inner class) is used for:
    // 1. Player: WHITE, BLACK
    // 2. Cell's content: WHITE, BLACK and NO_SEED
    public enum Seed {
        WHITE, BLACK, NO_SEED
    }
    private Seed currentPlayer; // the current player
    private Seed[][] board;     // Game board of ROWS-by-COLS cells

    // UI Components
    private GamePanel gamePanel; // Drawing canvas (JPanel) for the game board
    private JLabel statusBar;  // Status Bar

    /** Constructor to setup the game and the GUI components */
    public OthelloMain() {
        // Initialize the game objects
        initGame();

        // Set up GUI components
        gamePanel = new GamePanel();  // Construct a drawing canvas (a JPanel)
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // The canvas (JPanel) fires a MouseEvent upon mouse-click
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;
        
                // Prevent clicks after the game is over
                if (currentState != State.PLAYING) {
                    return;  // Ignore clicks if the game is over
                }
        
                if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == Seed.NO_SEED) {
                    // Check if the move is valid
                    if (isValidMove(currentPlayer, row, col)) {
                        // Update board[][] and return the new game state after the move
                        currentState = updateGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.WHITE) ? Seed.BLACK : Seed.WHITE;
                    }
                } else {  // If clicked after game over, restart the game
                    if (currentState != State.PLAYING) {
                        newGame();  // restart the game
                        repaint();  // refresh the canvas
                    }
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent().
            }
        });        

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        // Set up content pane
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();  // pack all the components in this JFrame
        setTitle("Othello");
        setVisible(true);  // show this JFrame
        setLocationRelativeTo(null);

        newGame();
    }

    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS]; // allocate array
        newGame();
    }

    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED; // all cells empty
            }
        }
        // Place initial seeds for Othello game
        board[3][3] = board[4][4] = Seed.WHITE;
        board[3][4] = board[4][3] = Seed.BLACK;

        currentPlayer = Seed.WHITE;    // white plays first
        currentState  = State.PLAYING; // ready to play
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, WHITE_WON, BLACK_WON).
     */
    public State updateGame(Seed mySeed, int rowSelected, int colSelected) {
        Seed opponentSeed = (mySeed == Seed.BLACK) ? Seed.WHITE : Seed.BLACK;
    
        // Place the player's seed on the board
        board[rowSelected][colSelected] = mySeed;
    
        // Directions for row/column/direction (8 directions)
        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};  // up-left, up, up-right, right, down-right, down, down-left, left
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};  // up-left, up, up-right, right, down-right, down, down-left, left
    
        // Loop over all 8 possible directions
        for (int i = 0; i < 8; i++) {
            int row = rowSelected + rowDirections[i];
            int col = colSelected + colDirections[i];
            boolean canFlip = false;
    
            // Find opponent's seeds in the current direction
            while (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == opponentSeed) {
                row += rowDirections[i];
                col += colDirections[i];
                canFlip = true;
            }
    
            // Check if we can flip the opponent's seeds
            if (canFlip && row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == mySeed) {
                // Flip the opponent's seeds in this direction
                row -= rowDirections[i];
                col -= colDirections[i];
                while (board[row][col] == opponentSeed) {
                    board[row][col] = mySeed;
                    row -= rowDirections[i];
                    col -= colDirections[i];
                }
            }
        }
    
        // Check if the game has ended
        if (hasNoValidMoves(Seed.WHITE) && hasNoValidMoves(Seed.BLACK)) {
            int whiteCount = 0, blackCount = 0;
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (board[r][c] == Seed.WHITE) whiteCount++;
                    if (board[r][c] == Seed.BLACK) blackCount++;
                }
            }
            if (whiteCount > blackCount) return State.WHITE_WON;
            if (blackCount > whiteCount) return State.BLACK_WON;
            return State.DRAW;  // Game is a draw
        }
    
        return State.PLAYING;  // Game still ongoing
    }    

    /** Check if the player has any valid moves left */
    private boolean hasNoValidMoves(Seed player) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED && isValidMove(player, row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This JPanel (inner class) will draw the game board.
     */
    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        // This is the method that does the actual drawing of the game.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);  // Always call super.paintComponent() first
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the background and the grid
            g2d.setColor(COLOR_BG);
            g2d.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

            // Draw the grid lines
            g2d.setColor(COLOR_GRID);
            g2d.setStroke(new BasicStroke(GRID_WIDTH));
            for (int row = 0; row <= ROWS; row++) {
                g2d.drawLine(0, row * CELL_SIZE, BOARD_WIDTH, row * CELL_SIZE);
            }
            for (int col = 0; col <= COLS; col++) {
                g2d.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, BOARD_HEIGHT);
            }

            // Draw the pieces (Black and White) inside the grid cells
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] == Seed.NO_SEED) continue;
                    Color color = (board[row][col] == Seed.WHITE) ? COLOR_WHITE : COLOR_BLACK;
                    g2d.setColor(color);
                    g2d.fillOval(col * CELL_SIZE + CELL_PADDING, row * CELL_SIZE + CELL_PADDING, SYMBOL_SIZE, SYMBOL_SIZE);
                }
            }

            // Show status (turn and winner)
            if (currentState == State.PLAYING) {
                statusBar.setText("Current Player: " + (currentPlayer == Seed.WHITE ? "White" : "Black"));
            } else if (currentState == State.WHITE_WON) {
                statusBar.setText("White Wins!");
            } else if (currentState == State.BLACK_WON) {
                statusBar.setText("Black Wins!");
            } else if (currentState == State.DRAW) {
                statusBar.setText("Game Draw!");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OthelloMain::new); // To ensure thread-safety for Swing components
    }
}