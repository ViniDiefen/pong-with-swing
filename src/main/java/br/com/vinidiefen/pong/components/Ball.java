package br.com.vinidiefen.pong.components;

import java.awt.Graphics;

import br.com.vinidiefen.pong.models.BallModel;
import br.com.vinidiefen.pong.physics.CollisionEvent;
import br.com.vinidiefen.pong.physics.CollisionObserver;
import br.com.vinidiefen.pong.renderers.BallRenderer;

/**
 * Ball entity that bounces around the screen
 */
public class Ball extends GameObject implements CollisionObserver {

    public static final int SIZE = 20;
    public static final int INITIAL_SPEED = 4;

    private int velocityX;
    private int velocityY;
    private int initialX;
    private int initialY;

    public Ball(int x, int y) {
        super(x, y, SIZE, SIZE);
        this.initialX = x;
        this.initialY = y;
        reset();
    }

    public Ball(BallModel model) {
        this(model.getX(), model.getY());
    }

    @Override
    public void update() {
        // Bounce off top and bottom walls
        if (y <= 0 || y + height >= parent.getHeight()) {
            velocityY = -velocityY;
            y = y <= 0 ? 0 : parent.getHeight() - height;
        }
        x += velocityX;
        y += velocityY;
    }

    @Override
    public void render(Graphics g) {
        BallRenderer.render(g, this);
    }

    /**
     * Reset ball to center with random direction
     */
    public void reset() {
        x = initialX;
        y = initialY;

        // Random direction (left or right)
        velocityX = (Math.random() < 0.5 ? -1 : 1) * INITIAL_SPEED;
        velocityY = (Math.random() < 0.5 ? -1 : 1) * INITIAL_SPEED;
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
        velocityX = velocityX > 0 ? velocityX + 1 : velocityX - 1;
        velocityY = velocityY > 0 ? velocityY + 1 : velocityY - 1;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void onCollisionChanged(CollisionEvent event) {
        reverseX();

        GameObject other = (GameObject) event.getSource();
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
