package br.com.vinidiefen.pong;

import java.awt.Graphics;

import br.com.vinidiefen.pong.renderers.ScoreRenderer;

/**
 * Manages player scores
 */
public class ScoreManager {

    private int leftScore;
    private int rightScore;
    private int winningScore;

    public ScoreManager(int winningScore) {
        this.winningScore = winningScore;
        this.leftScore = 0;
        this.rightScore = 0;
    }

    public void incrementLeftScore() {
        leftScore++;
    }

    public void incrementRightScore() {
        rightScore++;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
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
        if (leftScore >= winningScore)
            return 1;
        if (rightScore >= winningScore)
            return 2;
        return 0;
    }

    /**
     * Render scores on screen
     */
    public void render(Graphics g, int screenWidth, int screenHeight) {
        ScoreRenderer.render(g, leftScore, rightScore, screenWidth, screenHeight);
    }

}
