package br.com.vinidiefen.pong.infrastructure.persistence.models;

import java.util.UUID;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Column;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.ForeignKey;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Table;

@Table(name = "matches")
public class MatchModel {

    @Column(name = "id", type = "UUID", primaryKey = true)
    private UUID id;
    
    @ForeignKey(table = "paddles", column = "id")
    @Column(name = "left_paddle_id", type = "UUID", notNull = true)
    private UUID leftPaddleId;
    
    @ForeignKey(table = "paddles", column = "id")
    @Column(name = "right_paddle_id", type = "UUID", notNull = true)
    private UUID rightPaddleId;
    
    @ForeignKey(table = "balls", column = "id")
    @Column(name = "ball_id", type = "UUID", notNull = true)
    private UUID ballId;
    
    @ForeignKey(table = "score_manager", column = "id")
    @Column(name = "score_manager_id", type = "UUID", notNull = true)
    private UUID scoreManagerId;

    public MatchModel() {
        // Empty constructor for ORM
    }

    public MatchModel(UUID leftPaddleId, UUID rightPaddleId, UUID ballId, UUID scoreManagerId) {
        this.id = UUID.randomUUID();
        this.leftPaddleId = leftPaddleId;
        this.rightPaddleId = rightPaddleId;
        this.ballId = ballId;
        this.scoreManagerId = scoreManagerId;
    }
    
    // Convenience constructor that accepts Model objects and extracts their IDs
    public MatchModel(PaddleModel leftPaddle, PaddleModel rightPaddle, BallModel ball, ScoreManagerModel scoreManager) {
        this(leftPaddle.getId(), rightPaddle.getId(), ball.getId(), scoreManager.getId());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLeftPaddleId() {
        return leftPaddleId;
    }

    public void setLeftPaddleId(UUID leftPaddleId) {
        this.leftPaddleId = leftPaddleId;
    }

    public UUID getRightPaddleId() {
        return rightPaddleId;
    }

    public void setRightPaddleId(UUID rightPaddleId) {
        this.rightPaddleId = rightPaddleId;
    }

    public UUID getBallId() {
        return ballId;
    }

    public void setBallId(UUID ballId) {
        this.ballId = ballId;
    }

    public UUID getScoreManagerId() {
        return scoreManagerId;
    }

    public void setScoreManagerId(UUID scoreManagerId) {
        this.scoreManagerId = scoreManagerId;
    }

}
