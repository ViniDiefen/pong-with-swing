package br.com.vinidiefen.pong.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Abstract base class for game objects with position and size
 */
public abstract class GameObject {

    protected Component parent;

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public Component getParent() {
        return parent;
    }

    /**
     * Update entity state (position, velocity, etc.)
     */
    abstract void update();

    /**
     * Get the bounding box for collision detection
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Render the entity
     */
    public abstract void render(Graphics g);

}
