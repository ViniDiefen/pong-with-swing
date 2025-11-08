package br.com.vinidiefen.pong;

public class GameLoop extends Thread {

    private static final int TARGET_FPS = 60;
    private static final double NS_PER_UPDATE = 1_000_000_000.0 / TARGET_FPS;

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

        // Para calcular FPS e UPS (opcional)
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (gamePanel.isGameLoopActive()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NS_PER_UPDATE;
            lastTime = now;

            // Update
            while (delta >= 1) {
                if (gamePanel.gameLoopShouldUpdate()) {
                    gamePanel.updateComponents();
                }
                delta--;
            }

            // Render
            gamePanel.repaint();
            frames++;

            // FPS tracking (optional)
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }

            // Small sleep to prevent CPU overload
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
