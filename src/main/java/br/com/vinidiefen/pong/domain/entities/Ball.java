package br.com.vinidiefen.pong.domain.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

import br.com.vinidiefen.pong.infrastructure.persistence.models.BallModel;
import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.core.collision.CollisionObserver;

/**
 * Ball entity that bounces around the screen
 */
public class Ball extends GameObject implements CollisionObserver {

    // Constants moved to GameConstants
    private static final int SPEED_INCREMENT = GameConstants.BALL_SPEED_INCREMENT;

    private int velocityX;
    private int velocityY;
    private int initialX;
    private int initialY;

    public Ball(int x, int y) {
        super(x, y, GameConstants.BALL_SIZE, GameConstants.BALL_SIZE);
        this.initialX = x;
        this.initialY = y;
        reset();
    }

    public Ball(BallModel model) {
        this(model.getX(), model.getY());
    }

    @Override
    public void update() {
        handleVerticalBounds();
        x += velocityX;
        y += velocityY;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    /**
     * Reset ball to center with random direction
     */
    public void reset() {
        x = initialX;
        y = initialY;

        velocityX = randomDirection() * GameConstants.BALL_INITIAL_SPEED;
        velocityY = randomDirection() * GameConstants.BALL_INITIAL_SPEED;
    }

    /**
     * Reverse horizontal direction (when hitting paddle)
     */
    public void reverseX() {
        velocityX = -velocityX;
    }

    /**
     * Check if ball went off left side of screen
     */
    public boolean isOffLeft() {
        return x < 0;
    }

    /**
     * Check if ball went off right side of screen
     */
    public boolean isOffRight() {
        return x > parent.getWidth();
    }

    /**
     * Increase ball speed slightly
     */
    public void increaseSpeed() {
        velocityX += Integer.signum(velocityX) * SPEED_INCREMENT;
        velocityY += Integer.signum(velocityY) * SPEED_INCREMENT;
    }

    public int getVelocityX() {
        return velocityX;
    }
    
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }
    
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    private void handleVerticalBounds() {
        if (parent == null) {
            return;
        }

        boolean hitTop = y <= 0;
        boolean hitBottom = y + height >= parent.getHeight();
        if (hitTop || hitBottom) {
            velocityY = -velocityY;
            y = hitTop ? 0 : parent.getHeight() - height;
        }
    }

    private int randomDirection() {
        return ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
    }

    @Override
    public void onCollision(GameObject other) {
        reverseX();

        // Adjust ball position to prevent getting stuck
        if (getVelocityX() > 0) {
            setX(other.getX() + other.getWidth());
        } else {
            setX(other.getX() - getWidth());
        }

        // Increase speed on each hit for difficulty
        increaseSpeed();
    }

}
