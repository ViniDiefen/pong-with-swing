package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.components.Paddle;
import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "paddles")
public class PaddleModel extends GameObjectModel {

    @Column(name = "up_key", type = "INTEGER", notNull = true)
    private int upKey;
    @Column(name = "down_key", type = "INTEGER", notNull = true)
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
