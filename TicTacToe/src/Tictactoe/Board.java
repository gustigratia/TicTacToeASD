/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #8
 * 1 - 5026231083 - Arya Wiraguna Dwiputra
 * 2 - 5026231097 - Gusti Gratia Delpiera
 * 3 - 5026231152 - M. Naufal Erwin Effendi
 */
package Tictactoe;

public class Board {
    public static final int ROWS = 3;  // Standard 3x3 Tic-Tac-Toe
    public static final int COLS = 3;  // Standard 3x3 Tic-Tac-Toe
    public Cell[][] cells;
    private Seed currentPlayer;
    private boolean isSinglePlayer;

    public Board(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        cells = new Cell[ROWS][COLS];
        initGame();
    }

    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
        currentPlayer = Seed.NOUGHT;  // Let player start first in both cases
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                SoundEffect.CLICK.play();
                cells[row][col].newGame();  // Reset each cell
            }
        }
    }

    public Seed getCurrentPlayer() {
        return currentPlayer;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;
        // Check for win or draw
        return checkBoardState();
    }

    // Find the best move for AI (returns row and col)
    public int[] findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[2];  // Store row and col

        // Try all possible moves and find the best one for AI
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    cells[row][col].content = Seed.CROSS;  // AI is CROSS
                    int moveVal = minimax(0, false);  // Call MiniMax with the maximizer (AI's turn)
                    cells[row][col].content = Seed.NO_SEED;  // Undo the move

                    if (moveVal > bestVal) {
                        bestMove[0] = row;  // Set the row of the best move
                        bestMove[1] = col;  // Set the col of the best move
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;  // Return the best move (row and col)
    }

    private int minimax(int depth, boolean isMax) {
        State boardState = checkBoardState();
        if (boardState == State.CROSS_WON) {
            return 10 - depth;
        } else if (boardState == State.NOUGHT_WON) {
            return depth - 10;
        } else if (boardState == State.DRAW) {
            return 0;
        }

        int best;
        if (isMax) { // Maximizing for AI (CROSS)
            best = Integer.MIN_VALUE;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        cells[row][col].content = Seed.CROSS;
                        best = Math.max(best, minimax(depth + 1, false));
                        cells[row][col].content = Seed.NO_SEED;
                    }
                }
            }
        } else { // Minimizing for opponent (NOUGHT)
            best = Integer.MAX_VALUE;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        cells[row][col].content = Seed.NOUGHT;
                        best = Math.min(best, minimax(depth + 1, true));
                        cells[row][col].content = Seed.NO_SEED;
                    }
                }
            }
        }

        return best;
    }

    public State checkBoardState() {
        // Check rows, columns, diagonals
        for (int row = 0; row < ROWS; ++row) {
            if (cells[row][0].content != Seed.NO_SEED
                    && cells[row][0].content == cells[row][1].content
                    && cells[row][1].content == cells[row][2].content) {
                return (cells[row][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }

        for (int col = 0; col < COLS; ++col) {
            if (cells[0][col].content != Seed.NO_SEED
                    && cells[0][col].content == cells[1][col].content
                    && cells[1][col].content == cells[2][col].content) {
                return (cells[0][col].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }

        if (cells[0][0].content != Seed.NO_SEED
                && cells[0][0].content == cells[1][1].content
                && cells[1][1].content == cells[2][2].content) {
            return (cells[0][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        if (cells[0][2].content != Seed.NO_SEED
                && cells[0][2].content == cells[1][1].content
                && cells[1][1].content == cells[2][0].content) {
            return (cells[0][2].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Check for draw
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }

        return State.DRAW;
    }
}
