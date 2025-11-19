package br.com.vinidiefen.pong.domain.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import br.com.vinidiefen.pong.input.InputObserver;
import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.infrastructure.persistence.models.PaddleModel;

/**
 * Paddle entity controlled by player (Observer in Observer pattern)
 */
public class Paddle extends GameObject implements InputObserver {

    private boolean moveUpFlag = false;
    private boolean moveDownFlag = false;

    // Configurable key codes for movement
    private int upKey;
    private int downKey;

    public Paddle(int x, int y, int upKey, int downKey) {
        super(x, y, GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
        this.upKey = upKey;
        this.downKey = downKey;
    }

    public Paddle(PaddleModel model) {
        this(model.getX(), model.getY(), model.getUpKey(), model.getDownKey());
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
            velocityY -= GameConstants.PADDLE_SPEED;
        }
        if (moveDownFlag) {
            velocityY += GameConstants.PADDLE_SPEED;
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
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
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
