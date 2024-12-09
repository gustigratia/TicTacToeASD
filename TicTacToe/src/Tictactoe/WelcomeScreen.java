package Tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public WelcomeScreen() {
        setTitle("Welcome to Tic-Tac-Toe!");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Tic-Tac-Toe!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cp.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Button for "1 Player" (vs AI)
        JButton btnVsAI = new JButton("1 Player (vs AI)");
        btnVsAI.setFont(new Font("Arial", Font.PLAIN, 18));
        btnVsAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(true);
            }
        });

        // Button for "2 Players" (vs Human)
        JButton btnVsHuman = new JButton("2 Players (vs Human)");
        btnVsHuman.setFont(new Font("Arial", Font.PLAIN, 18));
        btnVsHuman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(false);
            }
        });

        buttonPanel.add(btnVsAI);
        buttonPanel.add(btnVsHuman);
        cp.add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startGame(boolean isSinglePlayer) {
        dispose();
        SwingUtilities.invokeLater(() -> new TicTacToe(isSinglePlayer));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
