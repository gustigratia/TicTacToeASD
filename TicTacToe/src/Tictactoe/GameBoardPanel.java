package Tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class
GameBoardPanel extends JPanel {
    private static final int CELL_SIZE = 120;
    private static final int SYMBOL_SIZE = CELL_SIZE - 20;
    private Board board;

    public GameBoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(CELL_SIZE * 3, CELL_SIZE * 3));  // 3x3 grid

        // Add mouse listener to handle player moves
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;

                // Pass the row and column to playerMove
                ((TicTacToe) SwingUtilities.getWindowAncestor(GameBoardPanel.this)).playerMove(row, col);
            }
        });
    }

    // Paint the game board and symbols
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawSymbols(g);
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);

        // Draw grid lines
        for (int i = 1; i < 3; i++) {
            g.fillRect(0, CELL_SIZE * i - 5, CELL_SIZE * 3, 10);  // Horizontal lines
            g.fillRect(CELL_SIZE * i - 5, 0, 10, CELL_SIZE * 3);  // Vertical lines
        }
    }

    private void drawSymbols(Graphics g) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int x = col * CELL_SIZE + 10;
                int y = row * CELL_SIZE + 10;

                // Draw cat image for CROSS (Seed.CROSS)
                if (board.cells[row][col].content == Seed.CROSS) {
                    Image catImage = Seed.CROSS.getImage();  // Get Image from Seed.CROSS
                    g.drawImage(catImage, x, y, SYMBOL_SIZE, SYMBOL_SIZE, this);  // Draw the image at the calculated position
                }
                // Draw dog image for NOUGHT (Seed.NOUGHT)
                else if (board.cells[row][col].content == Seed.NOUGHT) {
                    Image dogImage = Seed.NOUGHT.getImage();  // Get Image from Seed.NOUGHT
                    g.drawImage(dogImage, x, y, SYMBOL_SIZE, SYMBOL_SIZE, this);  // Draw the image at the calculated position
                }
            }
        }
    }
}

