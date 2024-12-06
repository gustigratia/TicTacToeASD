import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField nameField;
    private JButton startButton;
    private JComboBox<String> difficultyComboBox;

    public WelcomeScreen() {
        // Set up the welcome screen JFrame
        setTitle("Welcome to Sudoku");
        setSize(500, 500);  // Set size for welcome screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Load the background image
        ImageIcon backgroundIcon = new ImageIcon("src/2.jpg");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);

        // Create a custom panel to draw the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning

        // Create the label for "Enter your username"
        JLabel usernameLabel = new JLabel("Enter your username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setBounds(185, 290, 200, 20); // Set position for the label
        usernameLabel.setForeground(Color.WHITE); // Set text color to white

        // Create the text field for player name
        nameField = new JTextField();
        nameField.setBounds(125, 310, 250, 30); // Position the text field below the label

        // Create the difficulty combo box
        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficultyLevels);
        difficultyComboBox.setBounds(190, 355, 120, 30);

        // Create the start button
        startButton = new JButton("PLAY");
        startButton.setBounds(200, 400, 100, 40);

        // Add components to the panel
        backgroundPanel.add(usernameLabel); // Add label to panel
        backgroundPanel.add(nameField);     // Add text field to panel
        backgroundPanel.add(difficultyComboBox);  // Add combo box to panel
        backgroundPanel.add(startButton);   // Add button to panel

        // Add the panel to the frame
        add(backgroundPanel);

        // Button click action to start the game
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                String playerName = nameField.getText().trim();
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();

                if (!playerName.isEmpty()) {
                    // Dispose the welcome screen and start the main game window
                    dispose();
                    new SudokuMain(playerName, selectedDifficulty);  // Pass player's name and difficulty to the main game window
                } else {
                    SoundEffect.WRONG.play();
                    JOptionPane.showMessageDialog(WelcomeScreen.this, "Please enter your name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

}