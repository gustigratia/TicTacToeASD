package Tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameBoardPanel extends JPanel {
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int CELL_WIDTH = BOARD_WIDTH / 3;
    private static final int CELL_HEIGHT = BOARD_HEIGHT / 3;

    private Board board;

    public GameBoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // Add mouse listener to handle moves only inside the grid
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT) {
                    int row = y / CELL_HEIGHT;
                    int col = x / CELL_WIDTH;
                    ((TicTacToe) SwingUtilities.getWindowAncestor(GameBoardPanel.this)).playerMove(row, col);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);  // Draw background outside the board
        drawGrid(g);        // Draw the grid
        drawSymbols(g);     // Draw X and O
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, new Color(100, 149, 237), getWidth(), getHeight(), new Color(240, 248, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Set grid color and stroke for better visibility
        g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2d.setStroke(new BasicStroke(6));

        // Draw grid lines
        for (int i = 1; i < 3; i++) {
            g2d.drawLine(0, i * CELL_HEIGHT, BOARD_WIDTH, i * CELL_HEIGHT); // Horizontal
            g2d.drawLine(i * CELL_WIDTH, 0, i * CELL_WIDTH, BOARD_HEIGHT); // Vertical
        }

        // Draw board boundary
        g2d.setStroke(new BasicStroke(8));
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    }

    private void drawSymbols(Graphics g) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                int centerX = col * CELL_WIDTH + CELL_WIDTH / 2;
                int centerY = row * CELL_HEIGHT + CELL_HEIGHT / 2;

                if (board.cells[row][col].content == Seed.CROSS) {
                    drawCross(g, centerX, centerY);
                } else if (board.cells[row][col].content == Seed.NOUGHT) {
                    drawNought(g, centerX, centerY);
                }
            }
        }
    }

    private void drawCross(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        int size = Math.min(CELL_WIDTH, CELL_HEIGHT) / 3;

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(x - size + 5, y - size + 5, x + size + 5, y + size + 5);
        g2d.drawLine(x - size + 5, y + size + 5, x + size + 5, y - size + 5);

        // Draw gradient X
        GradientPaint gradient = new GradientPaint(x - size, y - size, Color.BLUE, x + size, y + size, Color.CYAN);
        g2d.setPaint(gradient);
        g2d.drawLine(x - size, y - size, x + size, y + size);
        g2d.drawLine(x - size, y + size, x + size, y - size);
    }

    private void drawNought(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        int size = Math.min(CELL_WIDTH, CELL_HEIGHT) / 3;

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black
        g2d.setStroke(new BasicStroke(8));
        g2d.drawOval(x - size + 5, y - size + 5, size * 2, size * 2);

        // Draw gradient O
        GradientPaint gradient = new GradientPaint(x - size, y - size, Color.ORANGE, x + size, y + size, Color.RED);
        g2d.setPaint(gradient);
        g2d.drawOval(x - size, y - size, size * 2, size * 2);
    }
}