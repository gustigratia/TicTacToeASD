package Tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private static final long serialVersionUID = 1L;

    private Board board;
    private GameBoardPanel boardPanel;
    private JButton btnNewGame = new JButton("New Game");
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private static final Font FONT_STATUS = new Font("Arial", Font.PLAIN, 16);
    private static final Color COLOR_BG_STATUS = new Color(220, 220, 220);
    private boolean isSinglePlayer;

    public TicTacToe(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        this.board = new Board(isSinglePlayer);
        this.boardPanel = new GameBoardPanel(board);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(boardPanel, BorderLayout.CENTER);
        cp.add(btnNewGame, BorderLayout.NORTH);

        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);
        cp.add(statusBar, BorderLayout.SOUTH);

        startNewGame();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic-Tac-Toe");
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void startNewGame() {
        board.newGame();
        currentPlayer = Seed.NOUGHT;
        currentState = State.PLAYING;
        updateStatusBar();
        boardPanel.repaint();
    }

    public void playerMove(int row, int col) {
        if (board.cells[row][col].content == Seed.NO_SEED && currentState == State.PLAYING) {
            currentState = board.stepGame(currentPlayer, row, col);
            boardPanel.repaint();
            updateStatusBar();

            if (currentState == State.PLAYING) {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                if (isSinglePlayer && currentPlayer == Seed.CROSS) {
                    aiMove();
                }
            }
        }
    }

    private void aiMove() {
        SwingWorker<Void, Void> aiWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1000);
                int[] bestMove = board.findBestMove();
                board.stepGame(Seed.CROSS, bestMove[0], bestMove[1]);
                return null;
            }

            @Override
            protected void done() {
                boardPanel.repaint();
                currentState = board.checkBoardState();
                updateStatusBar();

                if (currentState == currentState.PLAYING) {
                    currentPlayer = Seed.NOUGHT;
                }
            }
        };

        aiWorker.execute();
    }

    private void updateStatusBar() {
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click 'New Game' to play again");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click 'New Game' to play again");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click 'New Game' to play again");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
