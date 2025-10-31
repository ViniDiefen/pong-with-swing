package br.com.vinidiefen.pong.renderers;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Handles rendering of game field elements
 */
public class GameOverRenderer {

    /**
     * Draw game over screen
     * 
     * @param g            Graphics context
     * @param winner       Winner number (1 or 2)
     * @param screenWidth  Width of the screen
     * @param screenHeight Height of the screen
     */
    public static void drawGameOver(Graphics g, int winner, int screenWidth, int screenHeight) {
        if (screenWidth <= 0 || screenHeight <= 0) {
            return;
        }

        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, screenWidth, screenHeight);

        // Winner text
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 64));

        String winnerText = "Player " + winner + " Wins!";
        int textWidth = g.getFontMetrics().stringWidth(winnerText);
        g.drawString(winnerText, screenWidth / 2 - textWidth / 2, screenHeight / 2);

        // Instructions
        g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 32));
        String instructions = "Press ESC to exit";
        textWidth = g.getFontMetrics().stringWidth(instructions);
        g.drawString(instructions, screenWidth / 2 - textWidth / 2, screenHeight / 2 + 60);
    }

}
