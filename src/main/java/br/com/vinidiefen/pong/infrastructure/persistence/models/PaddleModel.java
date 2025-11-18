package br.com.vinidiefen.pong.infrastructure.persistence.models;

import java.util.UUID;

import br.com.vinidiefen.pong.domain.entities.Paddle;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Column;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Table;

@Table(name = "paddles")
public class PaddleModel {

    @Column(name = "id", type = "UUID", primaryKey = true)
    private UUID id;
    
    @Column(name = "x", type = "INTEGER", notNull = true)
    private int x;
    
    @Column(name = "y", type = "INTEGER", notNull = true)
    private int y;
    
    @Column(name = "up_key", type = "INTEGER", notNull = true)
    private int upKey;
    
    @Column(name = "down_key", type = "INTEGER", notNull = true)
    private int downKey;

    public PaddleModel() {
        // Empty constructor for ORM
    }

    private PaddleModel(Paddle paddle) {
        this.id = UUID.randomUUID();
        this.x = paddle.getX();
        this.y = paddle.getY();
        this.upKey = paddle.getUpKey();
        this.downKey = paddle.getDownKey();
    }

    /**
     * Factory method to create PaddleModel from Paddle component
     */
    public static PaddleModel from(Paddle paddle) {
        return new PaddleModel(paddle);
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

    public int getUpKey() {
        return upKey;
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    public int getDownKey() {
        return downKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }

}
