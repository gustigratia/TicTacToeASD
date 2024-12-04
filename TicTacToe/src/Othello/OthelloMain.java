package Othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO in one class
 */
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
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
    public static final Color COLOR_BG = new Color(173, 216, 230);  // background
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_GRID   = Color.gray;  // grid lines
    public static final Color COLOR_CROSS  = new Color(255, 255, 255);  // Red #D32D41
    public static final Color COLOR_NOUGHT = new Color(0, 0, 0); // Blue #4CB5F5
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // This enum (inner class) contains the various states of the game
    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }
    private State currentState;  // the current game state

    // This enum (inner class) is used for:
    // 1. Player: CROSS, NOUGHT
    // 2. Cell's content: CROSS, NOUGHT and NO_SEED
    public enum Seed {
        CROSS, NOUGHT, NO_SEED
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

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0
                            && col < COLS && board[row][col] == Seed.NO_SEED) {
                        // Update board[][] and return the new game state after the move
                        currentState = updateGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {       // game over
                    newGame(); // restart the game
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
        setTitle("Tic Tac Toe");
        setVisible(true);  // show this JFrame
        setLocationRelativeTo(null);

        newGame();
    }

    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS]; // allocate array
    }

    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState  = State.PLAYING; // ready to play
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */

    public State updateGame(Seed mySeed, int rowSelected, int colSelected) {
        Seed opponentSeed = (mySeed == Seed.NOUGHT) ? Seed.CROSS : Seed.NOUGHT;

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
                canFlip = true;  // Found an opponent's seed, can potentially flip
            }

            // After scanning, check if the last cell is the player's seed
            if (canFlip && row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == mySeed) {
                // Flip all the opponent's seeds between selected cell and the player's seed
                row = rowSelected + rowDirections[i];
                col = colSelected + colDirections[i];
                while (board[row][col] == opponentSeed) {
                    board[row][col] = mySeed; // Flip the seed
                    row += rowDirections[i];
                    col += colDirections[i];
                }
            }
        }

        // After making the move, check for game over
        currentState = checkGameOver();
//        if (currentState == State.PLAYING) {
//            currentPlayer = (currentPlayer == Seed.NOUGHT) ? Seed.CROSS : Seed.NOUGHT; // Switch player
//        }

        repaint();  // Refresh the UI after the move
        return currentState;
    }




    public State checkGameOver() {
        boolean boardFull = true;
        boolean hasValidMove = false;
        int cross_count = 0;
        int nought_count = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED) {
                    boardFull = false;
                }
                else if(board[row][col] == Seed.CROSS){
                    cross_count++;
                }
                else if(board[row][col] == Seed.NOUGHT){
                    nought_count++;
                }
//                if (isValidMove(Seed.CROSS, row, col) || isValidMove(Seed.NOUGHT, row, col)) {
//                    hasValidMove = true;
//                }
            }
        }

        if (boardFull && cross_count == nought_count) {
            return State.DRAW;
        } else if(boardFull && cross_count > nought_count) {
            return State.CROSS_WON;
        } else if(boardFull && nought_count > cross_count) {
            return State.NOUGHT_WON;
        }
        return State.PLAYING;
    }

    public boolean isValidMove(Seed player, int row, int col) {
        if (board[row][col] != Seed.NO_SEED) {
            return false; // Cell is already filled
        }
        // Check all 8 directions for a valid move
        int[] rowDirections = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] colDirections = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < 8; i++) {
            int r = row + rowDirections[i];
            int c = col + colDirections[i];
            boolean canFlip = false;

            while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] != Seed.NO_SEED) {
                if (board[r][c] == player) {
                    canFlip = true;
                    break;
                }
                r += rowDirections[i];
                c += colDirections[i];
            }

            if (canFlip) {
                return true;
            }
        }

        return false;
    }


    /**
     *  Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L; // to prevent serializable warning

        @Override
        public void paintComponent(Graphics g) {  // Callback via repaint()
            super.paintComponent(g);
            setBackground(COLOR_BG);  // set its background color

            // Draw the grid lines
            g.setColor(COLOR_GRID);
            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        BOARD_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, BOARD_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
            }

            // Draw the Seeds of all the cells if they are not empty
            // Use Graphics2D which allows us to set the pen's stroke
            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING/2;
                    int y1 = row * CELL_SIZE + CELL_PADDING/2;
                    int x2 = (col + 1) * CELL_SIZE ;
                    int y2 = (row + 1) * CELL_SIZE ;
                    if (board[row][col] == Seed.CROSS) {  // draw a 2-line cross
                        g2d.setColor(COLOR_CROSS);
//                        g2d.drawLine(x1, y1, x2, y2);
//                        g2d.drawLine(x2, y1, x1, y2);
                        g2d.fillOval(x1,y1,CELL_SIZE - CELL_PADDING,CELL_SIZE - CELL_PADDING);
                    } else if (board[row][col] == Seed.NOUGHT) {  // draw a circle
                        g2d.setColor(COLOR_NOUGHT);
//                        g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                        g2d.fillOval(x1,y1,CELL_SIZE - CELL_PADDING,CELL_SIZE - CELL_PADDING);
                    }
                }
            }

            // Print status message
            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
            } else if (currentState == State.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'White' Won! Click to play again");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'Black' Won! Click to play again");
            }
        }
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OthelloMain(); // Let the constructor do the job
            }
        });
    }
}