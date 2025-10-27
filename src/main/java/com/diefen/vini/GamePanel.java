package com.diefen.vini;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.diefen.vini.entities.Ball;
import com.diefen.vini.entities.FieldLine;
import com.diefen.vini.entities.Paddle;
import com.diefen.vini.input.KeyboardHandler;
import com.diefen.vini.physics.CollisionDetector;
import com.diefen.vini.renderers.GameOverRenderer;

/**
 * Game Panel com implementação de Game Loop
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int TARGET_FPS = 60;
    private static final double NS_PER_UPDATE = 1_000_000_000.0 / TARGET_FPS;
    private static final int WINNING_SCORE = 5;

    // Game entities
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private FieldLine fieldLine;
    private Ball ball;

    // Game systems
    private ScoreManager scoreManager;
    private KeyboardHandler keyboardHandler;
    private CollisionDetector collisionDetector;

    // Game loop control
    private Thread gameThread;
    private volatile GameState currentState = GameState.STOPPED;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        // Listen for component resize events to get actual dimensions
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Initialize game after we have real dimensions
                if (leftPaddle == null && getWidth() > 0 && getHeight() > 0) {
                    initializeGame();
                }
            }
        });
    }

    /**
     * Initialize all game objects and systems
     */
    private void initializeGame() {
        // Create paddles with configurable keys
        int paddleMargin = 50;
        leftPaddle = new Paddle(this, paddleMargin, getHeight() / 2 - Paddle.HEIGHT / 2,
                KeyEvent.VK_W, KeyEvent.VK_S);
        rightPaddle = new Paddle(this, getWidth() - paddleMargin - Paddle.WIDTH,
                getHeight() / 2 - Paddle.HEIGHT / 2,
                KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        // Create ball
        ball = new Ball(this,
                getWidth() / 2 - Ball.SIZE / 2,
                getHeight() / 2 - Ball.SIZE / 2);
        // Create field line
        fieldLine = new FieldLine(this);

        // Initialize systems
        scoreManager = new ScoreManager(WINNING_SCORE);
        keyboardHandler = new KeyboardHandler();
        collisionDetector = new CollisionDetector();

        // Register paddles as observers (Observer pattern)
        keyboardHandler.addInputObserver(leftPaddle);
        keyboardHandler.addInputObserver(rightPaddle);
        addKeyListener(keyboardHandler);

        // Register collision detection between ball and paddles
        collisionDetector.addCollisionObserver(ball, leftPaddle, rightPaddle);

        // Start game loop (on a separate thread)
        start();
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

        while (currentState.isGameLoopActive()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NS_PER_UPDATE;
            lastTime = now;

            // Input
            processInput();

            // Update
            while (delta >= 1) {
                if (currentState == GameState.PLAYING && leftPaddle != null) {
                    update();
                }
                delta--;
            }

            // Render
            repaint();
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

    /**
     * PROCESSA INPUT
     * 
     * Executado FORA do loop de delta
     * Garante responsividade máxima
     */
    private void processInput() {
        // No nosso caso, o KeyboardHandler já processa via eventos
        // Mas aqui poderíamos fazer polling adicional se necessário

        // Exemplo: verificar se ESC foi pressionado para pausar
        // ou qualquer processamento de input que não seja feito via eventos
    }

    /**
     * Update game entities and check game logic
     */
    private void update() {
        // Update entities with screen boundaries
        leftPaddle.update();
        rightPaddle.update();
        ball.update();

        // Check collisions
        collisionDetector.checkCollision();

        // Check scoring
        if (ball.isOffLeft()) {
            scoreManager.incrementRightScore();
            ball.reset();
            checkWinCondition();
        } else if (ball.isOffRight()) {
            scoreManager.incrementLeftScore();
            ball.reset();
            checkWinCondition();
        }
    }

    /**
     * Check if someone won
     */
    private void checkWinCondition() {
        if (scoreManager.hasWinner()) {
            currentState = GameState.GAME_OVER;
        }
    }

    /**
     * Render the game entities
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Don't draw anything until game is initialized
        if (leftPaddle == null || rightPaddle == null || ball == null) {
            return;
        }

        // Draw entities
        leftPaddle.render(g);
        rightPaddle.render(g);
        ball.render(g);
        fieldLine.render(g);

        // Draw scores
        scoreManager.render(g, getWidth(), getHeight());

        // Draw game over screen
        if (currentState == GameState.GAME_OVER) {
            GameOverRenderer.drawGameOver(g, scoreManager.getWinner(), getWidth(), getHeight());
        }

        // Sync for smooth rendering
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Starts the game loop in a new thread
     */
    public void start() {
        if (currentState.isGameLoopActive())
            return;

        currentState = GameState.PLAYING;
        gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }

    /**
     * Pause the game loop (does not stop the rendering)
     */
    public void pause() {
        currentState = GameState.PAUSED;
    }

    /**
     * Stop completely the game loop
     */
    public void stop() {
        currentState = GameState.STOPPED;
        try {
            if (gameThread != null) {
                // Wait 1 second for the thread to finish
                gameThread.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
