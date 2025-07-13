import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GoBoomGUI extends JFrame {
    private GoBoom game;
    private JTextArea textArea;
    private JButton playButton;
    private JButton saveButton;
    private JButton loadButton;

    public GoBoomGUI() {
        game = new GoBoom();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("GoBoom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playGame();
            }
        });
        buttonPanel.add(playButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
        buttonPanel.add(saveButton);

        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void playGame() {
        textArea.setText("");
        textArea.append("Starting a new game...\n\n");

        playButton.setEnabled(false); // Disable the Play button during gameplay

        // Create a new thread to run the game logic
        Thread gameThread = new Thread(new Runnable() {
            public void run() {
                game.playGame();

                // Enable the Play button once the game is finished
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        playButton.setEnabled(true);
                    }
                });
            }
        });

        // Start the game thread
        gameThread.start();
    }

    private void saveGame() {
        String filename = JOptionPane.showInputDialog(this, "Enter the filename to save the game:");
        if (filename != null && !filename.trim().isEmpty()) {
            game.saveGame(filename);
            textArea.append("Game saved successfully.\n");
        }
    }

    private void loadGame() {
        String filename = JOptionPane.showInputDialog(this, "Enter the filename to load the game:");
        if (filename != null && !filename.trim().isEmpty()) {
            game.loadGame(filename);
            textArea.append("Game loaded successfully.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GoBoomGUI();
            }
        });
    }
}