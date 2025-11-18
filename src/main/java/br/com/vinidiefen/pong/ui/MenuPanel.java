package br.com.vinidiefen.pong.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import br.com.vinidiefen.pong.constants.UIConstants;
import br.com.vinidiefen.pong.services.GameStateService;

/**
 * Menu Panel - Initial game screen
 */
public class MenuPanel extends JPanel {
    
    private final GameFrame gameFrame;
    private final GameStateService gameStateService;
    
    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.gameStateService = new GameStateService();
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        setupUI();
    }
    
    /**
     * Sets up the menu interface
     */
    private void setupUI() {
        // Panel for the title
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(UIConstants.BACKGROUND_COLOR);
        titlePanel.setPreferredSize(new Dimension(0, 300));
        
        // Title label
        JLabel titleLabel = new JLabel("PONG");
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Get font from UIManager and increase size for title
        Font baseFont = (Font) UIManager.get("Label.font");
        if (baseFont != null) {
            titleLabel.setFont(baseFont.deriveFont(UIConstants.TITLE_FONT_SIZE));
        } else {
            titleLabel.setFont(new Font("Arial", Font.BOLD, (int) UIConstants.TITLE_FONT_SIZE));
        }
        
        titlePanel.add(titleLabel);
        
        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Play button
        JButton playButton = ButtonFactory.createMenuButton("JOGAR");
        playButton.addActionListener(e -> startGame());
        gbc.gridy = 0;
        buttonPanel.add(playButton, gbc);
        
        // Load game button
        JButton loadButton = ButtonFactory.createMenuButton("CARREGAR");
        loadButton.addActionListener(e -> loadGame());
        gbc.gridy = 1;
        buttonPanel.add(loadButton, gbc);
        
        // Exit button
        JButton exitButton = ButtonFactory.createMenuButton("SAIR");
        exitButton.addActionListener(e -> exitGame());
        gbc.gridy = 2;
        buttonPanel.add(exitButton, gbc);
        
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Starts the game
     */
    private void startGame() {
        gameFrame.startGame();
    }
    
    /**
     * Loads a saved game
     */
    private void loadGame() {
        try {
            var matches = gameStateService.getAllMatches();
            
            if (matches.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nenhum jogo salvo encontrado!", 
                    "Carregar Jogo", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Show dialog to select a save
            SaveGameDialog.show(this, matches, gameStateService, this::loadSelectedGame);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar jogos salvos: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Loads the selected game
     */
    private void loadSelectedGame(UUID matchId) {
        gameFrame.loadGame(matchId);
    }
    
    /**
     * Exits the game
     */
    private void exitGame() {
        System.exit(0);
    }
}
