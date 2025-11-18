package br.com.vinidiefen.pong.models;

import java.util.UUID;

import br.com.vinidiefen.pong.components.ScoreManager;
import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.Table;

@Table(name = "score_manager")
public class ScoreManagerModel {

    @Column(name = "id", type = "UUID", primaryKey = true)
    private UUID id;
    
    @Column(name = "left_score", type = "INTEGER")
    public int leftScore;
    
    @Column(name = "right_score", type = "INTEGER")
    public int rightScore;
    
    @Column(name = "winning_score", type = "INTEGER")
    public int winningScore;

    public ScoreManagerModel() {
        // Empty constructor for ORM
    }

    private ScoreManagerModel(int winningScore, int leftScore, int rightScore) {
        this.id = UUID.randomUUID();
        this.winningScore = winningScore;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
    }

    /**
     * Factory method to create ScoreManagerModel from ScoreManager component
     */
    public static ScoreManagerModel from(ScoreManager scoreManager, int winningScore) {
        return new ScoreManagerModel(winningScore, scoreManager.getLeftScore(), scoreManager.getRightScore());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
