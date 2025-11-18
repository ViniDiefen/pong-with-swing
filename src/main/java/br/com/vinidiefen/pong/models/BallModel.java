package br.com.vinidiefen.pong.models;

import java.util.UUID;

import br.com.vinidiefen.pong.components.Ball;
import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "balls")
public class BallModel {

    @Column(name = "id", type = "UUID", primaryKey = true)
    private UUID id;
    
    @Column(name = "x", type = "INTEGER", notNull = true)
    private int x;
    
    @Column(name = "y", type = "INTEGER", notNull = true)
    private int y;
    
    @Column(name = "velocity_x", type = "INTEGER", notNull = true)
    private int velocityX;
    
    @Column(name = "velocity_y", type = "INTEGER", notNull = true)
    private int velocityY;

    public BallModel() {
        // Empty constructor for ORM
    }

    private BallModel(Ball ball) {
        this.id = UUID.randomUUID();
        this.x = ball.getX();
        this.y = ball.getY();
        this.velocityX = ball.getVelocityX();
        this.velocityY = ball.getVelocityY();
    }

    /**
     * Factory method to create BallModel from Ball component
     */
    public static BallModel from(Ball ball) {
        return new BallModel(ball);
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

}
