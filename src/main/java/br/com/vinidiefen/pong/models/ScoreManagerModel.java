package br.com.vinidiefen.pong.models;

import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "score_manager")
public class ScoreManagerModel extends UUIDObjectModel {

    @Column(name = "left_score", type = "INTEGER")
    public int leftScore;
    @Column(name = "right_score", type = "INTEGER")
    public int rightScore;
    @Column(name = "winning_score", type = "INTEGER")
    public int winningScore;

    public ScoreManagerModel() {
        super();
    }

    public ScoreManagerModel(int winningScore, int leftScore, int rightScore) {
        super();
        this.winningScore = winningScore;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public void setLeftScore(int leftScore) {
        this.leftScore = leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public void setRightScore(int rightScore) {
        this.rightScore = rightScore;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setWinningScore(int winningScore) {
        this.winningScore = winningScore;
    }

}
