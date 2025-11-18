package br.com.vinidiefen.pong.ui;

import java.util.UUID;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    public GameFrame() {
        super("Pong Game");

        // Window size and state
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // For better performance
        setResizable(true);

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
        gamePanel = new GamePanel();
        add(gamePanel);
        
        // Atualiza display
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
    
    /**
     * Carrega um jogo salvo
     */
    public void loadGame(UUID matchId) {
        // Remove menu panel
        if (menuPanel != null) {
            remove(menuPanel);
            menuPanel = null;
        }
        
        // Cria game panel e carrega o estado salvo
        gamePanel = new GamePanel();
        add(gamePanel);
        
        // Atualiza display
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
        
        // Carrega o jogo após a inicialização
        gamePanel.loadGameStateFromMenu(matchId);
    }
}