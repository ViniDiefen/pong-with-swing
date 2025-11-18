package br.com.vinidiefen.pong.constants;

import java.awt.event.KeyEvent;

/**
 * Input related constants (key mappings) used by the UI and input components.
 */
public final class InputConstants {

    private InputConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // Global
    public static final int EXIT_KEY = KeyEvent.VK_ESCAPE;

    // Left paddle keys
    public static final int LEFT_PADDLE_UP = KeyEvent.VK_W;
    public static final int LEFT_PADDLE_DOWN = KeyEvent.VK_S;

    // Right paddle keys
    public static final int RIGHT_PADDLE_UP = KeyEvent.VK_UP;
    public static final int RIGHT_PADDLE_DOWN = KeyEvent.VK_DOWN;

}
