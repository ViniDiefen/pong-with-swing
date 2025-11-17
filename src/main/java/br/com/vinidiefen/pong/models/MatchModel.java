package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.ForeignKey;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "matches")
public class MatchModel extends UUIDObjectModel {

    @ForeignKey(table = "paddles", column = "id")
    @Column(name = "left_paddle_id", type = "UUID", notNull = true)
    private PaddleModel leftPaddle;
    @ForeignKey(table = "paddles", column = "id")
    @Column(name = "right_paddle_id", type = "UUID", notNull = true)
    private PaddleModel rightPaddle;
    @ForeignKey(table = "balls", column = "id")
    @Column(name = "ball_id", type = "UUID", notNull = true)
    private BallModel ball;
    @ForeignKey(table = "score_manager", column = "id")
    @Column(name = "score_manager_id", type = "UUID", notNull = true)
    private ScoreManagerModel scoreManager;

    public MatchModel(PaddleModel leftPaddle, PaddleModel rightPaddle, BallModel ball, ScoreManagerModel scoreManager) {
        super();
        this.leftPaddle = leftPaddle;
        this.rightPaddle = rightPaddle;
        this.ball = ball;
        this.scoreManager = scoreManager;
    }

    public PaddleModel getLeftPaddle() {
        return leftPaddle;
    }

    public void setLeftPaddle(PaddleModel leftPaddle) {
        this.leftPaddle = leftPaddle;
    }

    public PaddleModel getRightPaddle() {
        return rightPaddle;
    }

    public void setRightPaddle(PaddleModel rightPaddle) {
        this.rightPaddle = rightPaddle;
    }

    public BallModel getBall() {
        return ball;
    }

    public void setBall(BallModel ball) {
        this.ball = ball;
    }

    public ScoreManagerModel getScoreManager() {
        return scoreManager;
    }

    public void setScoreManager(ScoreManagerModel scoreManager) {
        this.scoreManager = scoreManager;
    }

}
