import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    // Declare components
    private JTextField nameField;
    private JButton startButton;
    private JComboBox<String> difficultyComboBox;

    public WelcomeScreen() {
        // Set up the welcome screen JFrame
        setTitle("Welcome to Sudoku");
        setSize(400, 120);  // Set size for welcome screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Create a panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label and add it to the panel
        JLabel welcomeLabel = new JLabel("Enter your name", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Create a text field for player name
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 10));
        panel.add(nameField, BorderLayout.CENTER);

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficultyLevels);
        panel.add(difficultyComboBox, BorderLayout.WEST);

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
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();

                if (!playerName.isEmpty()) {
                    // Dispose the welcome screen and start the main game window
                    dispose();
                    new SudokuMain(playerName, selectedDifficulty);  // Pass player's name and difficulty to the main game window
                } else {
                    JOptionPane.showMessageDialog(WelcomeScreen.this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}
