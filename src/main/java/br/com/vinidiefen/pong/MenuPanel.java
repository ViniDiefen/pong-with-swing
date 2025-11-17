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
import java.util.List;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import br.com.vinidiefen.pong.models.MatchModel;
import br.com.vinidiefen.pong.repositories.CRUDRepository;

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
        
        // Load game button
        JButton loadButton = createStyledButton("CARREGAR");
        loadButton.addActionListener(e -> loadGame());
        gbc.gridy = 1;
        buttonPanel.add(loadButton, gbc);
        
        // Exit button
        JButton exitButton = createStyledButton("SAIR");
        exitButton.addActionListener(e -> exitGame());
        gbc.gridy = 2;
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
     * Loads a saved game
     */
    private void loadGame() {
        try {
            // Get all saved matches
            CRUDRepository<MatchModel> matchRepo = CRUDRepository.of(MatchModel.class);
            List<MatchModel> matches = matchRepo.findAll();
            
            if (matches.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Nenhum jogo salvo encontrado!", 
                    "Carregar Jogo", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create dialog to select a save
            JDialog dialog = new JDialog();
            dialog.setTitle("Selecione um Jogo Salvo");
            dialog.setModal(true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.getContentPane().setBackground(BACKGROUND_COLOR);
            
            // Title
            JLabel titleLabel = new JLabel("Jogos Salvos");
            titleLabel.setForeground(TITLE_COLOR);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            Font baseFont = (Font) UIManager.get("Label.font");
            if (baseFont != null) {
                titleLabel.setFont(baseFont.deriveFont(20f));
            }
            titleLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Create list model with save information
            DefaultListModel<String> listModel = new DefaultListModel<>();
            CRUDRepository<br.com.vinidiefen.pong.models.ScoreManagerModel> scoreRepo = 
                CRUDRepository.of(br.com.vinidiefen.pong.models.ScoreManagerModel.class);
            
            for (int i = 0; i < matches.size(); i++) {
                MatchModel match = matches.get(i);
                
                // Try to get score information
                String scoreInfo = "";
                try {
                    var scoreManager = scoreRepo.read(match.getScoreManagerId());
                    if (scoreManager != null) {
                        scoreInfo = String.format(" - Placar: %d x %d", 
                            scoreManager.getLeftScore(), scoreManager.getRightScore());
                    }
                } catch (Exception e) {
                    // If fails, just don't show score
                }
                
                String saveInfo = String.format("Save #%d%s", i + 1, scoreInfo);
                listModel.addElement(saveInfo);
            }
            
            // Create list
            JList<String> saveList = new JList<>(listModel);
            saveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            saveList.setBackground(new Color(30, 30, 30));
            saveList.setForeground(Color.WHITE);
            saveList.setFont(baseFont);
            saveList.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JScrollPane scrollPane = new JScrollPane(saveList);
            scrollPane.setBorder(new LineBorder(Color.WHITE, 1));
            
            // Button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(BACKGROUND_COLOR);
            
            JButton loadButton = createStyledButton("CARREGAR");
            JButton cancelButton = createStyledButton("CANCELAR");
            
            loadButton.addActionListener(e -> {
                int selectedIndex = saveList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    UUID selectedMatchId = matches.get(selectedIndex).getId();
                    dialog.dispose();
                    gameFrame.loadGame(selectedMatchId);
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Selecione um jogo para carregar!", 
                        "Aviso", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(loadButton);
            buttonPanel.add(cancelButton);
            
            // Add components to dialog
            dialog.add(titleLabel, BorderLayout.NORTH);
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
            
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
