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

import javax.swing.*;

import Othello.OthelloMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    public WelcomeScreen() {
        setTitle("Welcome to Tic-Tac-Toe and Othello!");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background GIF
        JLabel background = new JLabel(new ImageIcon("src/images/BG1.gif")); // Ensure "background.gif" exists
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // Welcome Label
        JLabel welcomeLabel = new JLabel(" ", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(255, 215, 0)); // Gold color
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        background.add(welcomeLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));

        // Button for "1 Player" (vs AI) in Tic-Tac-Toe
        JButton btnVsAI = createStyledButton("Tic-Tac-Toe: 1 Player (vs AI)");
        btnVsAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play(); // Ensure "click.wav" exists
                startTicTacToeGame(true);
            }
        });

        // Button for "2 Players" (vs Human) in Tic-Tac-Toe
        JButton btnVsHuman = createStyledButton("Tic-Tac-Toe: 2 Players (vs Human)");
        btnVsHuman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                startTicTacToeGame(false);
            }
        });

        // Button for Othello
        JButton btnOthello = createStyledButton("Play Othello");
        btnOthello.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                startOthelloGame();
            }
        });

        buttonPanel.add(btnVsAI);
        buttonPanel.add(btnVsHuman);
        buttonPanel.add(btnOthello);
        background.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        button.setBackground(new Color(30, 144, 255)); // Dodger Blue color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void startTicTacToeGame(boolean isSinglePlayer) {
        dispose();
        SwingUtilities.invokeLater(() -> new TicTacToe(isSinglePlayer));
    }

    private void startOthelloGame() {
        dispose();
        SwingUtilities.invokeLater(() -> new OthelloMain()); // Assumes Othello class exists
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File musicFile = new File(filePath);
            if (musicFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Background music file not found: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playClickSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.out.println("Click sound file not found: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WelcomeScreen();
        SoundEffect.BG.play();
        SoundEffect.BG.loop(); // Memulai musik
    }
}