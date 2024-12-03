import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    // Declare components
    private JTextField nameField;
    private JButton startButton;

    public WelcomeScreen() {
        // Set up the welcome screen JFrame
        setTitle("Welcome to Sudoku");
        setSize(400, 200);  // Set size for welcome screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Create a panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label and add it to the panel
        JLabel welcomeLabel = new JLabel("Welcome to Sudoku Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Create a text field for player name
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));
        panel.add(nameField, BorderLayout.CENTER);

        // Create a button to start the game
        startButton = new JButton("Start Game");
        panel.add(startButton, BorderLayout.SOUTH);

        // Add the panel to the frame
        add(panel);

        // Button click action to start the game
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();

                if (!playerName.isEmpty()) {
                    // Dispose the welcome screen and start the main game window
                    dispose();
                    new SudokuMain(playerName);  // Pass the player's name to the main game window
                } else {
                    JOptionPane.showMessageDialog(WelcomeScreen.this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Make the welcome screen visible
        setVisible(true);
    }

    public static void main(String[] args) {
        // Run the constructor of the WelcomeScreen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomeScreen();  // Show the welcome screen first
            }
        });
    }
}