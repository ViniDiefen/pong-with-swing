package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.components.Paddle;

public class PaddleModel extends GameObjectModel {

    private int upKey;
    private int downKey;

    public PaddleModel() {
        super();
    }

    public PaddleModel(Paddle paddle) {
        super(paddle);
        this.upKey = paddle.getUpKey();
        this.downKey = paddle.getDownKey();
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
