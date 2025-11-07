package br.com.vinidiefen.pong.models;

import java.util.UUID;

import br.com.vinidiefen.pong.components.GameObject;

public abstract class GameObjectModel {

    private UUID id;
    private int x;
    private int y;
    private int width;
    private int height;

    public GameObjectModel() {
    }

    public GameObjectModel(GameObject gameObject) {
        this.id = UUID.randomUUID();
        this.x = gameObject.getX();
        this.y = gameObject.getY();
        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}