package com.diefen.vini.renderers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.UIManager;

/**
 * Handles rendering of scores
 */
public class ScoreRenderer {

    /**
     * Render scores on screen
     * 
     * @param g            Graphics context
     * @param leftScore    Left player's score
     * @param rightScore   Right player's score
     * @param screenWidth  Width of the screen
     * @param screenHeight Height of the screen
     */
    public static void render(Graphics g, int leftScore, int rightScore, int screenWidth, int screenHeight) {
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
