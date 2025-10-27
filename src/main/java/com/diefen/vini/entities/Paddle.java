package com.diefen.vini.entities;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import com.diefen.vini.input.InputObserver;
import com.diefen.vini.renderers.PaddleRenderer;

/**
 * Paddle entity controlled by player (Observer in Observer pattern)
 */
public class Paddle extends GameObject implements InputObserver {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 100;
    public static final int SPEED = 5;

    private boolean moveUpFlag = false;
    private boolean moveDownFlag = false;

    // Configurable key codes for movement
    private int upKey;
    private int downKey;

    public Paddle(Component parent, int x, int y, int upKey, int downKey) {
        super(parent, x, y, WIDTH, HEIGHT);
        this.upKey = upKey;
        this.downKey = downKey;
    }

    /**
     * Set the key code for moving up
     * 
     * @param upKey Key code (e.g., KeyEvent.VK_W)
     */
    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    /**
     * Set the key code for moving down
     * 
     * @param downKey Key code (e.g., KeyEvent.VK_S)
     */
    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }

    /**
     * Get the key code for moving up
     */
    public int getUpKey() {
        return upKey;
    }

    /**
     * Get the key code for moving down
     */
    public int getDownKey() {
        return downKey;
    }

    @Override
    public void update() {
        // Calculate velocity based on flags
        int velocityY = 0;
        if (moveUpFlag) {
            velocityY -= SPEED;
        }
        if (moveDownFlag) {
            velocityY += SPEED;
        }

        y += velocityY;

        // Keep paddle within screen bounds
        if (y < 0) {
            y = 0;
        }
        if (y + height > parent.getHeight()) {
            y = parent.getHeight() - height;
        }
    }

    @Override
    public void render(Graphics g) {
        PaddleRenderer.render(g, this);
    }

    /**
     * Receive input events from KeyboardHandler
     */
    @Override
    public void onInputChanged(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (event.getID() == KeyEvent.KEY_PRESSED) {
            if (keyCode == upKey) {
                moveUpFlag = true;
            } else if (keyCode == downKey) {
                moveDownFlag = true;
            }
        } else if (event.getID() == KeyEvent.KEY_RELEASED) {
            if (keyCode == upKey) {
                moveUpFlag = false;
            } else if (keyCode == downKey) {
                moveDownFlag = false;
            }
        }
    }

}
