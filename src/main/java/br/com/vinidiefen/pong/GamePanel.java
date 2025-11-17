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
import java.util.UUID;

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
import br.com.vinidiefen.pong.models.BallModel;
import br.com.vinidiefen.pong.models.MatchModel;
import br.com.vinidiefen.pong.models.PaddleModel;
import br.com.vinidiefen.pong.models.ScoreManagerModel;
import br.com.vinidiefen.pong.physics.CollisionDetector;
import br.com.vinidiefen.pong.renderers.GameOverRenderer;
import br.com.vinidiefen.pong.repositories.CRUDRepository;

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
    private JButton loadButton;

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
                // Update load button position on resize
                if (loadButton != null) {
                    updateLoadButtonPosition();
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
        
        // Create load button (initially hidden)
        createLoadButton();

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
     * Creates the load button (visible only when paused)
     */
    private void createLoadButton() {
        loadButton = new JButton("LOAD");
        
        // Get font from UIManager
        Font buttonFont = (Font) UIManager.get("Label.font");
        if (buttonFont != null) {
            loadButton.setFont(buttonFont.deriveFont(12f));
        }
        
        // Button style
        loadButton.setPreferredSize(new Dimension(80, 40));
        loadButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        loadButton.setBorder(new LineBorder(Color.WHITE, 2));
        loadButton.setFocusPainted(false);
        loadButton.setContentAreaFilled(false);
        loadButton.setOpaque(false);
        loadButton.setVisible(false); // Initially hidden
        
        // Hover effect
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadButton.setForeground(new Color(100, 200, 255));
                loadButton.setBorder(new LineBorder(new Color(100, 200, 255), 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loadButton.setForeground(Color.WHITE);
                loadButton.setBorder(new LineBorder(Color.WHITE, 2));
            }
        });
        
        // Load game state on click
        loadButton.addActionListener(e -> loadGameState());
        
        updateLoadButtonPosition();
        add(loadButton);
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
     * Updates the load button position below the save button
     */
    private void updateLoadButtonPosition() {
        int margin = 10;
        int buttonWidth = 80;
        int buttonHeight = 40;
        int spacing = 10;
        // Position below save button
        loadButton.setBounds(getWidth() - buttonWidth - margin, margin + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight);
    }
    
    /**
     * Toggles between playing and paused states
     */
    private void togglePause() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
            pauseButton.setText("PLAY");
            saveButton.setVisible(true); // Show save button when paused
            loadButton.setVisible(true); // Show load button when paused
        } else if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            pauseButton.setText("PAUSE");
            saveButton.setVisible(false); // Hide save button when playing
            loadButton.setVisible(false); // Hide load button when playing
        }
        repaint();
        requestFocusInWindow(); // Return focus to panel for keyboard input
    }
    
    /**
     * Saves the current game state
     */
    private void saveGameState() {
        try {
            System.out.println("=== SAVING GAME STATE ===");
            
            // Create model instances from current game state
            PaddleModel leftPaddleModel = new PaddleModel(leftPaddle);
            PaddleModel rightPaddleModel = new PaddleModel(rightPaddle);
            BallModel ballModel = new BallModel(ball);
            ScoreManagerModel scoreManagerModel = new ScoreManagerModel(
                WINNING_SCORE,
                scoreManager.getLeftScore(),
                scoreManager.getRightScore()
            );
            
            // Create repositories
            CRUDRepository<PaddleModel> paddleRepo = CRUDRepository.of(PaddleModel.class);
            CRUDRepository<BallModel> ballRepo = CRUDRepository.of(BallModel.class);
            CRUDRepository<ScoreManagerModel> scoreRepo = CRUDRepository.of(ScoreManagerModel.class);
            CRUDRepository<MatchModel> matchRepo = CRUDRepository.of(MatchModel.class);
            
            // Save entities to database
            paddleRepo.create(leftPaddleModel);
            paddleRepo.create(rightPaddleModel);
            ballRepo.create(ballModel);
            scoreRepo.create(scoreManagerModel);
            
            // Create and save match with references
            MatchModel matchModel = new MatchModel(leftPaddleModel, rightPaddleModel, ballModel, scoreManagerModel);
            matchRepo.create(matchModel);
            
            System.out.println("Game state saved successfully to database!");
            System.out.println("Match ID: " + matchModel.getId());
            System.out.println("========================");
            
            // Visual feedback
            saveButton.setText("SAVED!");
            saveButton.setForeground(new Color(100, 200, 100));
            
        } catch (Exception e) {
            System.err.println("Error saving game state: " + e.getMessage());
            e.printStackTrace();
            
            // Error feedback
            saveButton.setText("ERROR!");
            saveButton.setForeground(new Color(200, 100, 100));
        }
        
        resetButtonAfterDelay(saveButton, "SAVE");
    }
    
    /**
     * Loads the most recent saved game state from the database
     */
    private void loadGameState() {
        try {
            System.out.println("=== LOADING GAME STATE ===");
            
            // Create repositories
            CRUDRepository<MatchModel> matchRepo = CRUDRepository.of(MatchModel.class);
            CRUDRepository<PaddleModel> paddleRepo = CRUDRepository.of(PaddleModel.class);
            CRUDRepository<BallModel> ballRepo = CRUDRepository.of(BallModel.class);
            CRUDRepository<ScoreManagerModel> scoreRepo = CRUDRepository.of(ScoreManagerModel.class);
            
            // Get all matches and find the most recent one
            var matches = matchRepo.findAll();
            if (matches.isEmpty()) {
                System.out.println("No saved games found!");
                loadButton.setText("EMPTY!");
                loadButton.setForeground(new Color(200, 200, 100));
                resetButtonAfterDelay(loadButton, "LOAD");
                return;
            }
            
            // Get the last match (most recent)
            MatchModel lastMatch = matches.get(matches.size() - 1);
            
            // Load related entities using their IDs
            PaddleModel leftPaddleModel = paddleRepo.read(lastMatch.getLeftPaddleId());
            PaddleModel rightPaddleModel = paddleRepo.read(lastMatch.getRightPaddleId());
            BallModel ballModel = ballRepo.read(lastMatch.getBallId());
            ScoreManagerModel scoreManagerModel = scoreRepo.read(lastMatch.getScoreManagerId());
            
            // Restore paddle positions
            leftPaddle.setX(leftPaddleModel.getX());
            leftPaddle.setY(leftPaddleModel.getY());
            rightPaddle.setX(rightPaddleModel.getX());
            rightPaddle.setY(rightPaddleModel.getY());
            
            // Restore ball position and velocity
            ball.setX(ballModel.getX());
            ball.setY(ballModel.getY());
            ball.setVelocityX(ballModel.getVelocityX());
            ball.setVelocityY(ballModel.getVelocityY());
            
            // Restore scores
            scoreManager.setLeftScore(scoreManagerModel.getLeftScore());
            scoreManager.setRightScore(scoreManagerModel.getRightScore());
            
            System.out.println("Game state loaded successfully!");
            System.out.println("Match ID: " + lastMatch.getId());
            System.out.println("Left Score: " + scoreManager.getLeftScore());
            System.out.println("Right Score: " + scoreManager.getRightScore());
            System.out.println("========================");
            
            // Visual feedback
            loadButton.setText("OK!");
            loadButton.setForeground(new Color(100, 200, 255));
            
            // Repaint to show changes
            repaint();
            
        } catch (Exception e) {
            System.err.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
            
            // Error feedback
            loadButton.setText("ERROR!");
            loadButton.setForeground(new Color(200, 100, 100));
        }
        
        resetButtonAfterDelay(loadButton, "LOAD");
    }
    
    /**
     * Public method to load game state from menu (without button feedback)
     */
    public void loadGameStateFromMenu(UUID matchId) {
        // Wait for components to be initialized
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("=== LOADING GAME STATE FROM MENU ===");
                System.out.println("Match ID: " + matchId);
                
                // Create repositories
                CRUDRepository<MatchModel> matchRepo = CRUDRepository.of(MatchModel.class);
                CRUDRepository<PaddleModel> paddleRepo = CRUDRepository.of(PaddleModel.class);
                CRUDRepository<BallModel> ballRepo = CRUDRepository.of(BallModel.class);
                CRUDRepository<ScoreManagerModel> scoreRepo = CRUDRepository.of(ScoreManagerModel.class);
                
                // Load the specific match
                MatchModel match = matchRepo.read(matchId);
                if (match == null) {
                    System.out.println("Match not found! Starting new game.");
                    return;
                }
                
                // Load related entities using their IDs
                PaddleModel leftPaddleModel = paddleRepo.read(match.getLeftPaddleId());
                PaddleModel rightPaddleModel = paddleRepo.read(match.getRightPaddleId());
                BallModel ballModel = ballRepo.read(match.getBallId());
                ScoreManagerModel scoreManagerModel = scoreRepo.read(match.getScoreManagerId());
                
                // Restore paddle positions
                leftPaddle.setX(leftPaddleModel.getX());
                leftPaddle.setY(leftPaddleModel.getY());
                rightPaddle.setX(rightPaddleModel.getX());
                rightPaddle.setY(rightPaddleModel.getY());
                
                // Restore ball position and velocity
                ball.setX(ballModel.getX());
                ball.setY(ballModel.getY());
                ball.setVelocityX(ballModel.getVelocityX());
                ball.setVelocityY(ballModel.getVelocityY());
                
                // Restore scores
                scoreManager.setLeftScore(scoreManagerModel.getLeftScore());
                scoreManager.setRightScore(scoreManagerModel.getRightScore());
                
                System.out.println("Game state loaded successfully!");
                System.out.println("Left Score: " + scoreManager.getLeftScore());
                System.out.println("Right Score: " + scoreManager.getRightScore());
                System.out.println("========================================");
                
                // Repaint to show changes
                repaint();
                
            } catch (Exception e) {
                System.err.println("Error loading game state: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Helper method to reset button text after a delay
     */
    private void resetButtonAfterDelay(JButton button, String originalText) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                SwingUtilities.invokeLater(() -> {
                    button.setText(originalText);
                    button.setForeground(Color.WHITE);
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
