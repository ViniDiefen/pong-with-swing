package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.components.GameObject;
import br.com.vinidiefen.pong.repositories.annotations.Column;

public abstract class GameObjectModel extends UUIDObjectModel {

    @Column(name = "x", type = "INTEGER", notNull = true)
    protected int x;
    @Column(name = "y", type = "INTEGER", notNull = true)
    protected int y;

    public GameObjectModel() {
        super();
    }

    public GameObjectModel(GameObject gameObject) {
        super();
        this.x = gameObject.getX();
        this.y = gameObject.getY();
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