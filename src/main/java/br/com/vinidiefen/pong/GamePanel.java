package br.com.vinidiefen.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import br.com.vinidiefen.pong.components.Ball;
import br.com.vinidiefen.pong.components.FieldLine;
import br.com.vinidiefen.pong.components.Paddle;
import br.com.vinidiefen.pong.input.GameShortcuts;
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
    private GameShortcuts shortcuts;

    // Game states/loop control
    private GameLoop gameLoopThread;
    private volatile GameState currentState = GameState.STOPPED;
    
    // UI Components
    private JButton pauseButton;
    private JButton saveButton;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null); // Use absolute positioning for the pause button

        // Listen for component resize events to get actual dimensions
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Initialize game after we have real dimensions
                if (leftPaddle == null && getWidth() > 0 && getHeight() > 0) {
                    initializeGame();
                }
                // Update pause button position on resize
                if (pauseButton != null) {
                    updatePauseButtonPosition();
                }
                // Update save button position on resize
                if (saveButton != null) {
                    updateSaveButtonPosition();
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
        int middleScreenY = getHeight() / 2;
        int middleScreenX = getWidth() / 2;

        int paddleMargin = 50;
        int middlePaddleY = Paddle.HEIGHT / 2;

        int middleBall = Ball.SIZE / 2;

        leftPaddle = new Paddle(paddleMargin, middleScreenY - middlePaddleY, KeyEvent.VK_W, KeyEvent.VK_S);
        rightPaddle = new Paddle(getWidth() - paddleMargin - Paddle.WIDTH, middleScreenY - middlePaddleY, KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        // Create ball
        ball = new Ball(middleScreenX - middleBall, middleScreenY - middleBall);
        // Create field line
        fieldLine = new FieldLine();

        // Set up parent references
        leftPaddle.setParent(this);
        rightPaddle.setParent(this);
        ball.setParent(this);
        fieldLine.setParent(this);

        // Initialize systems
        scoreManager = new ScoreManager(WINNING_SCORE);
        collisionDetector = new CollisionDetector();
        keyboardHandler = new KeyboardHandler();
        shortcuts = new GameShortcuts();

        // Register paddles as observers (Observer pattern)
        keyboardHandler.addInputObserver(leftPaddle);
        keyboardHandler.addInputObserver(rightPaddle);
        keyboardHandler.addInputObserver(shortcuts);
        addKeyListener(keyboardHandler);

        // Register collision detection between ball and paddles
        collisionDetector.addCollisionObserver(ball, leftPaddle, rightPaddle);
        
        // Create pause button
        createPauseButton();
        
        // Create save button (initially hidden)
        createSaveButton();

        // Start game loop thread
        currentState = GameState.PLAYING;
        gameLoopThread.start();
    }
    
    /**
     * Creates the pause button in the top-right corner
     */
    private void createPauseButton() {
        pauseButton = new JButton("PAUSE");
        
        // Get font from UIManager
        Font buttonFont = (Font) UIManager.get("Label.font");
        if (buttonFont != null) {
            pauseButton.setFont(buttonFont.deriveFont(12f));
        }
        
        // Button style
        pauseButton.setPreferredSize(new Dimension(80, 40));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        pauseButton.setBorder(new LineBorder(Color.WHITE, 2));
        pauseButton.setFocusPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setOpaque(false);
        
        // Hover effect
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pauseButton.setForeground(new Color(100, 200, 100));
                pauseButton.setBorder(new LineBorder(new Color(100, 200, 100), 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                pauseButton.setForeground(Color.WHITE);
                pauseButton.setBorder(new LineBorder(Color.WHITE, 2));
            }
        });
        
        // Toggle pause on click
        pauseButton.addActionListener(e -> togglePause());
        
        updatePauseButtonPosition();
        add(pauseButton);
    }
    
    /**
     * Creates the save button (visible only when paused)
     */
    private void createSaveButton() {
        saveButton = new JButton("SAVE");
        
        // Get font from UIManager
        Font buttonFont = (Font) UIManager.get("Label.font");
        if (buttonFont != null) {
            saveButton.setFont(buttonFont.deriveFont(12f));
        }
        
        // Button style
        saveButton.setPreferredSize(new Dimension(80, 40));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        saveButton.setBorder(new LineBorder(Color.WHITE, 2));
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.setOpaque(false);
        saveButton.setVisible(false); // Initially hidden
        
        // Hover effect
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveButton.setForeground(new Color(100, 200, 100));
                saveButton.setBorder(new LineBorder(new Color(100, 200, 100), 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                saveButton.setForeground(Color.WHITE);
                saveButton.setBorder(new LineBorder(Color.WHITE, 2));
            }
        });
        
        // Save game state on click
        saveButton.addActionListener(e -> saveGameState());
        
        updateSaveButtonPosition();
        add(saveButton);
    }
    
    /**
     * Updates the pause button position in the top-right corner
     */
    private void updatePauseButtonPosition() {
        int margin = 10;
        int buttonWidth = 80;
        int buttonHeight = 40;
        pauseButton.setBounds(getWidth() - buttonWidth - margin, margin, buttonWidth, buttonHeight);
    }
    
    /**
     * Updates the save button position below the pause button
     */
    private void updateSaveButtonPosition() {
        int margin = 10;
        int buttonWidth = 80;
        int buttonHeight = 40;
        int spacing = 10;
        // Position below pause button
        saveButton.setBounds(getWidth() - buttonWidth - margin, margin + buttonHeight + spacing, buttonWidth, buttonHeight);
    }
    
    /**
     * Toggles between playing and paused states
     */
    private void togglePause() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
            pauseButton.setText("PLAY");
            saveButton.setVisible(true); // Show save button when paused
        } else if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            pauseButton.setText("PAUSE");
            saveButton.setVisible(false); // Hide save button when playing
        }
        repaint();
        requestFocusInWindow(); // Return focus to panel for keyboard input
    }
    
    /**
     * Saves the current game state
     */
    private void saveGameState() {
        // TODO: Implement game state persistence
        System.out.println("=== SAVING GAME STATE ===");
        System.out.println("Left Paddle Score: " + scoreManager.getLeftScore());
        System.out.println("Right Paddle Score: " + scoreManager.getRightScore());
        System.out.println("Ball Position: (" + ball.getX() + ", " + ball.getY() + ")");
        System.out.println("Left Paddle Position: (" + leftPaddle.getX() + ", " + leftPaddle.getY() + ")");
        System.out.println("Right Paddle Position: (" + rightPaddle.getX() + ", " + rightPaddle.getY() + ")");
        System.out.println("========================");
        
        // Visual feedback
        saveButton.setText("SAVED!");
        saveButton.setForeground(new Color(100, 200, 100));
        
        // Reset button text after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                SwingUtilities.invokeLater(() -> {
                    saveButton.setText("SAVE");
                    saveButton.setForeground(Color.WHITE);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
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
        
        // Draw paused overlay
        if (currentState == GameState.PAUSED) {
            drawPausedOverlay(g);
        }

        // Sync for smooth rendering
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Draws the paused overlay
     */
    private void drawPausedOverlay(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // "PAUSED" text
        Font font = (Font) UIManager.get("Label.font");
        if (font != null) {
            g.setFont(font.deriveFont(48f));
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 48));
        }
        
        g.setColor(Color.WHITE);
        String text = "PAUSED";
        int textWidth = g.getFontMetrics().stringWidth(text);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2;
        g.drawString(text, x, y);
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
