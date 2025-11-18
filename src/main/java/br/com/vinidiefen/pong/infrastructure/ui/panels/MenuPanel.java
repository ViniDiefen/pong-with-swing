package br.com.vinidiefen.pong.infrastructure.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.vinidiefen.pong.application.services.GameStateService;
import br.com.vinidiefen.pong.constants.UIConstants;
import br.com.vinidiefen.pong.infrastructure.ui.GameFrame;
import br.com.vinidiefen.pong.infrastructure.ui.factories.ButtonFactory;
import br.com.vinidiefen.pong.infrastructure.ui.utils.FontUtils;

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
        titleLabel.setFont(FontUtils.getDefaultFont(UIConstants.TITLE_FONT_SIZE));

        titlePanel.add(titleLabel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Play button
        JButton playButton = ButtonFactory.createMenuButton(UIConstants.BTN_JOGAR);
        playButton.addActionListener(e -> startGame());
        gbc.gridy = 0;
        buttonPanel.add(playButton, gbc);

        // Load game button
        JButton loadButton = ButtonFactory.createMenuButton(UIConstants.BTN_CARREGAR);
        loadButton.addActionListener(e -> loadGame());
        gbc.gridy = 1;
        buttonPanel.add(loadButton, gbc);

        // Exit button
        JButton exitButton = ButtonFactory.createMenuButton(UIConstants.BTN_SAIR);
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

            // Show dialog to select a save
            SaveGameDialog.show(this, matches, gameStateService, (var id) -> {
                gameFrame.setMatchId(id);
                gameFrame.startGame();
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar jogos salvos: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exits the game
     */
    private void exitGame() {
        System.exit(0);
    }
}
