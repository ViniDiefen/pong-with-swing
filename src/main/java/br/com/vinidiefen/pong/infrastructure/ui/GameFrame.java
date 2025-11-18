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
     * Mostra o menu inicial
     */
    public void showMenu() {
        // Remove game panel se existir
        if (gamePanel != null) {
            remove(gamePanel);
            gamePanel.stop();
            gamePanel = null;
            matchId = null;
        }
        
        // Cria e adiciona menu panel
        menuPanel = new MenuPanel(this);
        add(menuPanel);
        
        // Atualiza display
        revalidate();
        repaint();
        menuPanel.requestFocusInWindow();
    }
    
    /**
     * Inicia o jogo (chamado pelo menu)
     */
    public void startGame() {
        // Remove menu panel
        if (menuPanel != null) {
            remove(menuPanel);
            menuPanel = null;
        }
        
        // Cria e adiciona game panel
        if (matchId != null) {
            gamePanel = new GamePanel(matchId);
        } else {
            gamePanel = new GamePanel();
        }
        add(gamePanel);
        
        // Atualiza display
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
    
    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

}