package br.com.vinidiefen.pong.models;

import java.util.UUID;

import br.com.vinidiefen.pong.components.GameObject;
import br.com.vinidiefen.pong.repositories.annotations.Column;

public abstract class GameObjectModel {

    @Column(name = "id", type = "UUID", primaryKey = true)
    protected UUID id;
    @Column(name = "x", type = "INTEGER", notNull = true)
    protected int x;
    @Column(name = "y", type = "INTEGER", notNull = true)
    protected int y;

    public GameObjectModel() {
    }

    public GameObjectModel(GameObject gameObject) {
        this.id = UUID.randomUUID();
        this.x = gameObject.getX();
        this.y = gameObject.getY();
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

}