package br.com.vinidiefen.pong.constants;

import java.awt.Color;

/**
 * UI-related constants for consistent styling across the application
 */
public final class UIConstants {
    
    private UIConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // Colors
    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color BUTTON_HOVER_COLOR = new Color(100, 200, 100);
    public static final Color BUTTON_HOVER_BLUE = new Color(100, 200, 255);
    public static final Color BUTTON_HOVER_YELLOW = new Color(200, 200, 100);
    public static final Color SUCCESS_COLOR = new Color(100, 200, 100);
    public static final Color ERROR_COLOR = new Color(200, 100, 100);
    public static final Color WARNING_COLOR = new Color(200, 200, 100);
    public static final Color OVERLAY_COLOR = new Color(0, 0, 0, 150);
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 180);
    public static final Color LIST_BACKGROUND = new Color(30, 30, 30);
    
    // Sizes
    public static final int BUTTON_MARGIN = 10;
    public static final int SMALL_BUTTON_WIDTH = 80;
    public static final int SMALL_BUTTON_HEIGHT = 40;
    public static final int MENU_BUTTON_WIDTH = 200;
    public static final int MENU_BUTTON_HEIGHT = 50;
    public static final int BUTTON_SPACING = 10;
    
    // Font sizes
    public static final float TITLE_FONT_SIZE = 48f;
    public static final float SUBTITLE_FONT_SIZE = 20f;
    public static final float GAME_BUTTON_FONT_SIZE = 12f;
    public static final float SCORE_FONT_SIZE = 48f;
    public static final float LARGE_TEXT_SIZE = 64f;
    public static final float MEDIUM_TEXT_SIZE = 32f;
    public static final float PAUSED_TEXT_SIZE = 48f;
    
    // Delays
    public static final int BUTTON_RESET_DELAY_MS = 2000;
    public static final int BUTTON_FEEDBACK_DELAY = 2000; // milliseconds
    
    // Score rendering positions
    public static final int SCORE_TEXT_Y = 60;

    // Game over UI
    public static final int GAME_OVER_INSTRUCTION_OFFSET = 60;

    // Field line dashing pattern
    public static final float[] FIELD_LINE_DASH_PATTERN = { 10f, 10f };
    
    // Button text constants
    public static final String BTN_PAUSE = "PAUSE";
    public static final String BTN_PLAY = "PLAY";
    public static final String BTN_SAVE = "SAVE";
    public static final String BTN_LOAD = "LOAD";
    public static final String BTN_JOGAR = "JOGAR";
    public static final String BTN_CARREGAR = "CARREGAR";
    public static final String BTN_SAIR = "SAIR";
    
    // Button feedback messages
    public static final String BTN_FEEDBACK_SAVED = "SAVED!";
    public static final String BTN_FEEDBACK_OK = "OK!";
    public static final String BTN_FEEDBACK_ERROR = "ERROR!";
    public static final String BTN_FEEDBACK_EMPTY = "EMPTY!";
}
