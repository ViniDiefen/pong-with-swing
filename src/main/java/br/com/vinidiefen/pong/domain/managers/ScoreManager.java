package br.com.vinidiefen.pong.domain.managers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Optional;

import br.com.vinidiefen.pong.constants.UIConstants;
import br.com.vinidiefen.pong.infrastructure.ui.utils.FontUtils;

/**
 * Manages game scores and win condition
 */
public class ScoreManager {

    public enum Player {
        LEFT,
        RIGHT;

        public int getWinnerCode() {
            return this == LEFT ? 1 : 2;
        }
    }

    private int leftScore;
    private int rightScore;
    private int winningScore;
    private final Font scoreFont;

    public ScoreManager(int winningScore) {
        this.winningScore = winningScore;
        this.scoreFont = resolveScoreFont();
        reset();
    }

    public void incrementLeftScore() {
        incrementScore(Player.LEFT);
    }

    public void incrementRightScore() {
        incrementScore(Player.RIGHT);
    }

    public void incrementScore(Player player) {
        addPoints(player, 1);
    }

    public void addPoints(Player player, int points) {
        if (player == null || points <= 0) {
            return;
        }

        if (player == Player.LEFT) {
            leftScore += points;
        } else {
            rightScore += points;
        }
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

    public void reset() {
        leftScore = 0;
        rightScore = 0;
    }

    /**
     * Check if either player has won
     */
    public boolean hasWinner() {
        return leftScore >= winningScore || rightScore >= winningScore;
    }

    /**
     * Get the winner (1 for left, 2 for right, 0 for no winner)
     */
    public int getWinner() {
        return getWinnerPlayer().map(Player::getWinnerCode).orElse(0);
    }

    /**
     * Returns the winner, if any, as a type-safe value.
     */
    public Optional<Player> getWinnerPlayer() {
        if (leftScore >= winningScore) {
            return Optional.of(Player.LEFT);
        }
        if (rightScore >= winningScore) {
            return Optional.of(Player.RIGHT);
        }
        return Optional.empty();
    }

    /**
     * Render scores on screen
     */
    public void render(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(Color.WHITE);
        g.setFont(scoreFont);

        drawScore(g, screenWidth / 4, leftScore);
        drawScore(g, 3 * screenWidth / 4, rightScore);
    }

    private void drawScore(Graphics g, int xPosition, int score) {
        g.drawString(String.valueOf(score), xPosition, UIConstants.SCORE_TEXT_Y);
    }

    private Font resolveScoreFont() {
        return FontUtils.getDefaultFont(Font.PLAIN, UIConstants.SCORE_FONT_SIZE);
    }

}
