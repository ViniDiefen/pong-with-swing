package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.components.Ball;

public class BallModel extends GameObjectModel {

    private int velocityX;
    private int velocityY;

    public BallModel() {

    }

    public BallModel(Ball ball) {
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
