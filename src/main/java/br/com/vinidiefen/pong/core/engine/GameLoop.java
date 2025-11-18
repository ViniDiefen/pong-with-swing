package br.com.vinidiefen.pong.core.engine;

import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.infrastructure.ui.panels.GamePanel;

/**
 * Game loop runs independently of UI rendering; moved to core.engine package.
 */
public class GameLoop extends Thread {

    private static final double NS_PER_UPDATE = 1_000_000_000.0 / GameConstants.TARGET_FPS;

    private GamePanel gamePanel;

    public GameLoop(GamePanel gamePanel) {
        super("Game Loop Thread");
        this.gamePanel = gamePanel;
    }

    /**
     * Game loop
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (gamePanel.isGameLoopActive()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NS_PER_UPDATE;
            lastTime = now;

            // Update
            if (delta >= 1) {
                if (gamePanel.gameLoopShouldUpdate()) {
                    gamePanel.updateComponents();
                }
                delta--;
            }

            // Render
            gamePanel.repaint();

            // Small sleep to prevent CPU overload
            try {
                Thread.sleep(GameConstants.GAME_LOOP_SLEEP_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
