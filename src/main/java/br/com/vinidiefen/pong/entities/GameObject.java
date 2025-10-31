package br.com.vinidiefen.pong.entities;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Abstract base class for game objects with position and size
 */
public abstract class GameObject extends Entity {

    protected Component parent;
    protected int width;
    protected int height;

    public GameObject(Component parent, int x, int y, int width, int height) {
        super(x, y);
        this.parent = parent;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
