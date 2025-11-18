package br.com.vinidiefen.pong.constants;

/**
 * Game-related constants for consistent gameplay
 */
public final class GameConstants {
    
    private GameConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // Game settings
    public static final int WINNING_SCORE = 5;
    public static final int TARGET_FPS = 60;
    
    // Paddle settings
    public static final int PADDLE_WIDTH = 20;
    public static final int PADDLE_HEIGHT = 100;
    public static final int PADDLE_SPEED = 5;
    public static final int PADDLE_MARGIN = 50;
    
    // Ball settings
    public static final int BALL_SIZE = 20;
    public static final int BALL_INITIAL_SPEED = 4;
    public static final int BALL_SPEED_INCREMENT = 1;
    
    // Shortcuts are centralized in InputConstants
    
    // Field settings
    public static final int FIELD_LINE_WIDTH = 3;
    
    // Engine behavior
    public static final int GAME_LOOP_SLEEP_MS = 1;
}
