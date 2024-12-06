import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");

    // Constructor
    public SudokuMain(String playerName, String selectedDifficulty) {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);
        JLabel difficultyLabel = new JLabel("Difficulty: " + selectedDifficulty, SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        cp.add(difficultyLabel, BorderLayout.SOUTH);

        // Add a button to the south to re-start the game via board.newGame()
        cp.add(btnNewGame, BorderLayout.SOUTH);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SoundEffect.CLICK.play();
//                board.newGame(selectedDifficulty);
                new WelcomeScreen();
            }
        });

        // Initialize the game board to start the game
        board.newGame(selectedDifficulty);

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle(playerName + "'s Sudoku");
        setVisible(true);
        setLocationRelativeTo(null);
    }


    /** The entry main() entry method */
    public static void main(String[] args) {
        // Run the constructor of the WelcomeScreen first
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SoundEffect.initGame();  // Memuat semua file audio
                SoundEffect.SONG.play();
                SoundEffect.SONG.loop();// Memulai musik
                new WelcomeScreen();  // Show the welcome screen first
            }
        });
    }
}