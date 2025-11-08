package br.com.vinidiefen.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import br.com.vinidiefen.pong.entities.Ball;
import br.com.vinidiefen.pong.entities.FieldLine;
import br.com.vinidiefen.pong.entities.Paddle;
import br.com.vinidiefen.pong.input.KeyboardHandler;
import br.com.vinidiefen.pong.physics.CollisionDetector;
import br.com.vinidiefen.pong.renderers.GameOverRenderer;

/**
 * Game Panel com implementação de Game Loop
 */
public class GamePanel extends JPanel {
    
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

    // Game states/loop control
    private GameLoop gameLoopThread;
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
        // Instance the game loop thread
        this.gameLoopThread = new GameLoop(this);

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

        // Start game loop thread
        currentState = GameState.PLAYING;
        gameLoopThread.start();
    }

    /**
     * Update game entities and check game logic
     */
    public void updateComponents() {
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
     * Stop completely the game loop
     */
    public void stop() {
        currentState = GameState.STOPPED;
        try {
            if (gameLoopThread != null) {
                // Wait 1 second for the thread to finish
                gameLoopThread.join(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isGameLoopActive() {
        return currentState.isGameLoopActive();
    }

    public boolean gameLoopShouldUpdate() {
        return currentState == GameState.PLAYING;
    }

}
