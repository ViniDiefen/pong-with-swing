package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.components.Ball;
import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "balls")
public class BallModel extends GameObjectModel {

    @Column(name = "velocity_x", type = "INTEGER", notNull = true)
    private int velocityX;
    @Column(name = "velocity_y", type = "INTEGER", notNull = true)
    private int velocityY;

    public BallModel() {
        super();
    }

    public BallModel(Ball ball) {
        super(ball);
        this.velocityX = ball.getVelocityX();
        this.velocityY = ball.getVelocityY();
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
