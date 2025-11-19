package br.com.vinidiefen.pong.infrastructure.ui;

import java.util.UUID;

import javax.swing.JFrame;

import br.com.vinidiefen.pong.infrastructure.ui.panels.MenuPanel;
import br.com.vinidiefen.pong.infrastructure.ui.panels.GamePanel;

public class GameFrame extends JFrame {
    
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private UUID matchId;

    public GameFrame() {
        super("Pong Game");

        // Window size and state
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // For better performance
        setResizable(false);

        // Focus settings for keyboard input
        setFocusable(true);
        requestFocusInWindow();
    }
    
    /**
     * Shows the main menu
     */
    public void showMenu() {
        // Remove game panel if exists
        if (gamePanel != null) {
            remove(gamePanel);
            gamePanel.stop();
            gamePanel = null;
            matchId = null;
        }
        
        // Create and add menu panel
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        
        // Update display
        revalidate();
        repaint();
        menuPanel.requestFocusInWindow();
    }
    
    /**
     * Starts the game (called by menu)
     */
    public void startGame() {
        // Remove menu panel
        if (menuPanel != null) {
            remove(menuPanel);
            menuPanel = null;
        }
        
        // Create and add game panel
        if (matchId != null) {
            gamePanel = new GamePanel(matchId);
        } else {
            gamePanel = new GamePanel();
        }
        add(gamePanel);
        
        // Update display
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
    
    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

}