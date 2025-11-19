package br.com.vinidiefen.pong.infrastructure.ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.com.vinidiefen.pong.application.services.GameStateService;
import br.com.vinidiefen.pong.application.services.GameStateService.LoadedGameState;
import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.constants.GameState;
import br.com.vinidiefen.pong.constants.InputConstants;
import br.com.vinidiefen.pong.constants.UIConstants;
import br.com.vinidiefen.pong.core.collision.CollisionDetector;
import br.com.vinidiefen.pong.core.engine.GameLoop;
import br.com.vinidiefen.pong.domain.entities.Ball;
import br.com.vinidiefen.pong.domain.entities.FieldLine;
import br.com.vinidiefen.pong.domain.entities.Paddle;
import br.com.vinidiefen.pong.domain.managers.ScoreManager;
import br.com.vinidiefen.pong.infrastructure.ui.factories.ButtonFactory;
import br.com.vinidiefen.pong.infrastructure.ui.utils.FontUtils;
import br.com.vinidiefen.pong.input.handlers.GameShortcuts;
import br.com.vinidiefen.pong.input.handlers.KeyboardHandler;

/**
 * Game Panel where the Pong game runs
 */
public class GamePanel extends JPanel {

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
    private GameStateService gameStateService;

    // Game states/loop control
    private GameLoop gameLoopThread;
    private volatile GameState currentState = GameState.STOPPED;

    // Match ID to load on initialization
    private UUID pendingMatchIdToLoad;

    // UI Components
    private JButton pauseButton;
    private JButton saveButton;
    private JButton loadButton;

    public GamePanel() {
        this(null);
    }

    public GamePanel(UUID matchId) {
        setBackground(Color.BLACK);
        setFocusable(true);
        // Use absolute positioning for the pause button
        setLayout(null); // Use absolute positioning for the pause button

        this.pendingMatchIdToLoad = matchId;
        // Listen for component resize events to get actual dimensions
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Initialize game after we have real dimensions
                if (leftPaddle == null && getWidth() > 0 && getHeight() > 0) {
                    initializeGame();
                }
                // Update all button positions on resize
                updateAllButtonPositions();
            }
        });
    }

    /**
     * Update positions of all buttons
     */
    private void updateAllButtonPositions() {
        if (pauseButton != null) {
            updateButtonPosition(pauseButton, 0);
        }
        if (saveButton != null) {
            updateButtonPosition(saveButton, 1);
        }
        if (loadButton != null) {
            updateButtonPosition(loadButton, 2);
        }
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

        int paddleMargin = GameConstants.PADDLE_MARGIN;
        int middlePaddleY = GameConstants.PADDLE_HEIGHT / 2;

        int middleBall = GameConstants.BALL_SIZE / 2;
        leftPaddle = new Paddle(paddleMargin, middleScreenY - middlePaddleY, InputConstants.LEFT_PADDLE_UP,
                InputConstants.LEFT_PADDLE_DOWN);
        rightPaddle = new Paddle(getWidth() - paddleMargin - GameConstants.PADDLE_WIDTH, middleScreenY - middlePaddleY,
                InputConstants.RIGHT_PADDLE_UP, InputConstants.RIGHT_PADDLE_DOWN);
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
        scoreManager = new ScoreManager(GameConstants.WINNING_SCORE);
        collisionDetector = new CollisionDetector();
        keyboardHandler = new KeyboardHandler();
        shortcuts = new GameShortcuts();
        gameStateService = new GameStateService();

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

        // Start game loop with Thread-based implementation
        currentState = GameState.PLAYING;
        gameLoopThread.start();

        // If there's a pending match to load, load it now
        if (pendingMatchIdToLoad != null) {
            try {
                LoadedGameState state = gameStateService.loadGameState(pendingMatchIdToLoad);
                applyLoadedState(state);
                pendingMatchIdToLoad = null; // Clear the pending load
                repaint();
            } catch (Exception e) {
                System.err.println("Error loading pending game state: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the pause button in the top-right corner
     */
    private void createPauseButton() {
        pauseButton = ButtonFactory.createPauseButton();
        pauseButton.addActionListener(e -> togglePause());
        updateButtonPosition(pauseButton, 0);
        add(pauseButton);
    }

    /**
     * Creates the save button (visible only when paused)
     */
    private void createSaveButton() {
        saveButton = ButtonFactory.createSaveButton();
        saveButton.setVisible(false);
        saveButton.addActionListener(e -> saveGameState());
        updateButtonPosition(saveButton, 1);
        add(saveButton);
    }

    /**
     * Creates the load button (visible only when paused)
     */
    private void createLoadButton() {
        loadButton = ButtonFactory.createLoadButton();
        loadButton.setVisible(false);
        loadButton.addActionListener(e -> loadGameState());
        updateButtonPosition(loadButton, 2);
        add(loadButton);
    }

    /**
     * Updates button position in the top-right corner stack
     * 
     * @param button The button to position
     * @param index  The stack index (0 = top, 1 = second, 2 = third)
     */
    private void updateButtonPosition(JButton button, int index) {
        int x = getWidth() - UIConstants.SMALL_BUTTON_WIDTH - UIConstants.BUTTON_MARGIN;
        int y = UIConstants.BUTTON_MARGIN + index * (UIConstants.SMALL_BUTTON_HEIGHT + UIConstants.BUTTON_SPACING);
        button.setBounds(x, y, UIConstants.SMALL_BUTTON_WIDTH, UIConstants.SMALL_BUTTON_HEIGHT);
    }

    /**
     * Toggles between playing and paused states
     */
    private void togglePause() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
            pauseButton.setText(UIConstants.BTN_PLAY);
            saveButton.setVisible(true); // Show save button when paused
            loadButton.setVisible(true); // Show load button when paused
        } else if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
            pauseButton.setText(UIConstants.BTN_PAUSE);
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
            gameStateService.saveGameState(leftPaddle, rightPaddle, ball, scoreManager);
            setButtonFeedback(saveButton, UIConstants.BTN_FEEDBACK_SAVED, UIConstants.SUCCESS_COLOR); // Visual feedback
        } catch (Exception e) {
            System.err.println("Error saving game state: " + e.getMessage());
            setButtonFeedback(saveButton, UIConstants.BTN_FEEDBACK_ERROR, UIConstants.ERROR_COLOR); // Error feedback
        }
    }

    /**
     * Loads the most recent saved game state from the database
     */
    private void loadGameState() {
        try {
            var matches = gameStateService.getAllMatches();
            if (matches.isEmpty()) {
                System.out.println("No saved games found!");
                setButtonFeedback(loadButton, UIConstants.BTN_FEEDBACK_EMPTY, UIConstants.WARNING_COLOR);
                return;
            }

            // Get the last match (most recent)
            UUID lastMatchId = matches.get(matches.size() - 1).getId();
            LoadedGameState state = gameStateService.loadGameState(lastMatchId);

            // Apply loaded state
            applyLoadedState(state);

            // Visual feedback
            setButtonFeedback(loadButton, UIConstants.BTN_FEEDBACK_OK, UIConstants.BUTTON_HOVER_BLUE);

            // Repaint to show changes
            repaint();

        } catch (Exception e) {
            System.err.println("Error loading game state: " + e.getMessage());

            // Error feedback
            setButtonFeedback(loadButton, UIConstants.BTN_FEEDBACK_ERROR, UIConstants.ERROR_COLOR);
        }
    }

    /**
     * Applies a loaded game state to the current game components
     */
    private void applyLoadedState(LoadedGameState state) {
        // Restore paddle positions
        leftPaddle.setX(state.getLeftPaddle().getX());
        leftPaddle.setY(state.getLeftPaddle().getY());
        rightPaddle.setX(state.getRightPaddle().getX());
        rightPaddle.setY(state.getRightPaddle().getY());

        // Restore ball position and velocity
        ball.setX(state.getBall().getX());
        ball.setY(state.getBall().getY());
        ball.setVelocityX(state.getBall().getVelocityX());
        ball.setVelocityY(state.getBall().getVelocityY());

        // Restore scores
        scoreManager.setLeftScore(state.getScoreManager().getLeftScore());
        scoreManager.setRightScore(state.getScoreManager().getRightScore());
    }

    /**
     * Sets button visual feedback (text and color)
     */
    private void setButtonFeedback(JButton button, String text, Color color) {
        String previousString = button.getText();
        button.setText(text);
        button.setForeground(color);
        resetButtonAfterDelay(button, previousString);
    }

    /**
     * Resets button appearance after a delay using Swing Timer (thread-safe)
     */
    private void resetButtonAfterDelay(JButton button, String originalText) {
        javax.swing.Timer timer = new javax.swing.Timer(UIConstants.BUTTON_FEEDBACK_DELAY, e -> {
            button.setText(originalText);
            button.setForeground(UIConstants.TEXT_COLOR);
        });
        timer.setRepeats(false); // Execute only once
        timer.start();
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
            drawGameOver(g);
        }

        // Draw paused overlay
        if (currentState == GameState.PAUSED) {
            drawPausedOverlay(g);
        }

        // Sync for smooth rendering
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Draws the game over screen
     */
    private void drawGameOver(Graphics g) {
        drawSemiTransparentOverlay(g);

        // Winner text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, (int) UIConstants.LARGE_TEXT_SIZE));

        String winnerText = "Player " + scoreManager.getWinner() + " Wins!";
        int textWidth = g.getFontMetrics().stringWidth(winnerText);
        g.drawString(winnerText, getWidth() / 2 - textWidth / 2, getHeight() / 2);

        // Instructions
        g.setFont(new Font("Arial", Font.PLAIN, (int) UIConstants.MEDIUM_TEXT_SIZE));
        String instructions = "Press ESC to exit";
        textWidth = g.getFontMetrics().stringWidth(instructions);
        g.drawString(instructions, getWidth() / 2 - textWidth / 2,
                getHeight() / 2 + UIConstants.GAME_OVER_INSTRUCTION_OFFSET);
    }

    /**
     * Draws the paused overlay
     */
    private void drawPausedOverlay(Graphics g) {
        drawSemiTransparentOverlay(g);

        // "PAUSED" text
        g.setFont(FontUtils.getDefaultFont(Font.BOLD, UIConstants.PAUSED_TEXT_SIZE));

        g.setColor(UIConstants.TEXT_COLOR);
        String text = "PAUSED";
        int textWidth = g.getFontMetrics().stringWidth(text);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2;
        g.drawString(text, x, y);
    }

    /**
     * Draws a semi-transparent overlay covering the entire panel
     */
    private void drawSemiTransparentOverlay(Graphics g) {
        g.setColor(UIConstants.OVERLAY_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Stop the game completely
     */
    public void stop() {
        currentState = GameState.STOPPED;
        // Thread will stop automatically when isGameLoopActive() returns false
    }

    public boolean isGameLoopActive() {
        return currentState.isGameLoopActive();
    }

    public boolean gameLoopShouldUpdate() {
        return currentState == GameState.PLAYING;
    }

}
