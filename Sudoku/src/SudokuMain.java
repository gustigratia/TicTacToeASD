import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // private variables
    GameBoardPanel board = new GameBoardPanel();
    JButton btnNewGame = new JButton("New Game");

    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        // Add a button to the south to re-start the game via board.newGame()
        cp.add(btnNewGame, BorderLayout.SOUTH);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.newGame();
                playSound("C:/Users/gusti/Music/Feast & The Panturas  - Gelora (Official Music Video).mp3");
            }
        });

        // Initialize the game board to start the game
        board.newGame();
        playSound("C:/Users/gusti/Music/Feast & The Panturas  - Gelora (Official Music Video).mp3");

        pack();     // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    // Method to play sound (audio file)
    private void playSound(String filePath) {
        try {
            // Load the audio file (make sure the path is correct)
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a Clip object for playback
            Clip clip = AudioSystem.getClip();

            // Open the clip with the audio stream
            clip.open(audioStream);

            // Start playing the audio
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** The entry main() entry method */
    public static void main(String[] args) {
        // Run the constructor of the WelcomeScreen first
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                new WelcomeScreen();  // Show the welcome screen first
            }
        });
    }
}