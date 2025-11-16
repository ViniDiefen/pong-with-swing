package br.com.vinidiefen.pong;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * Menu Panel - Initial game screen
 */
public class MenuPanel extends JPanel {
    
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color TITLE_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = Color.WHITE;
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 200, 100);
    
    private final GameFrame gameFrame;
    
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        setupUI();
    }
    
    /**
     * Sets up the menu interface
     */
    private void setupUI() {
        // Panel for the title
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setPreferredSize(new Dimension(0, 300));
        
        // Title label
        JLabel titleLabel = new JLabel("PONG");
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Get font from UIManager and increase size for title
        Font baseFont = (Font) UIManager.get("Label.font");
        if (baseFont != null) {
            titleLabel.setFont(baseFont.deriveFont(48f));
        } else {
            titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        }
        
        titlePanel.add(titleLabel);
        
        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Play button
        JButton playButton = createStyledButton("JOGAR");
        playButton.addActionListener(e -> startGame());
        gbc.gridy = 0;
        buttonPanel.add(playButton, gbc);
        
        // Exit button
        JButton exitButton = createStyledButton("SAIR");
        exitButton.addActionListener(e -> exitGame());
        gbc.gridy = 1;
        buttonPanel.add(exitButton, gbc);
        
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates a styled button for the menu
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        
        // Get font from UIManager
        Font buttonFont = (Font) UIManager.get("Label.font");
        if (buttonFont != null) {
            button.setFont(buttonFont);
        }
        
        // Button style
        button.setPreferredSize(new Dimension(200, 50));
        button.setForeground(BUTTON_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setBorder(new LineBorder(BUTTON_COLOR, 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(BUTTON_HOVER_COLOR);
                button.setBorder(new LineBorder(BUTTON_HOVER_COLOR, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(BUTTON_COLOR);
                button.setBorder(new LineBorder(BUTTON_COLOR, 2));
            }
        });
        
        return button;
    }
    
    /**
     * Starts the game
     */
    private void startGame() {
        gameFrame.startGame();
    }
    
    /**
     * Exits the game
     */
    private void exitGame() {
        System.exit(0);
    }
}
