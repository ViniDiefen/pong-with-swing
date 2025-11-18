package br.com.vinidiefen.pong.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.UIManager;

/**
 * Manages game scores and win condition
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
        g.setColor(Color.WHITE);
        g.setFont(UIManager.getFont("Label.font").deriveFont(Font.PLAIN, 48f));

        // Draw left score
        String leftScoreStr = String.valueOf(leftScore);
        int leftX = screenWidth / 4;
        g.drawString(leftScoreStr, leftX, 60);

        // Draw right score
        String rightScoreStr = String.valueOf(rightScore);
        int rightX = 3 * screenWidth / 4;
        g.drawString(rightScoreStr, rightX, 60);
    }

}
