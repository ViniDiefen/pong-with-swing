package com.diefen.vini;

import java.awt.Font;
import java.io.InputStream;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for Pong game
 */
public class Main {

    public static void main(String[] args) {
        // Run on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            configureDefaultFont();

            // Create and setup game
            GameFrame gameFrame = new GameFrame();
            GamePanel gamePanel = new GamePanel();

            gameFrame.add(gamePanel);
            gameFrame.setVisible(true);

            // Request focus for keyboard input
            gamePanel.requestFocusInWindow();
        });
    }

    /**
     * Configure custom font for UI
     */
    private static void configureDefaultFont() {
        Font customFont = loadFont();
        if (customFont != null) {
            customFont = customFont.deriveFont(24f);
            UIManager.put("Label.font", customFont);
        }
    }

    /**
     * Load custom font from resources
     */
    public static Font loadFont() {
        try (InputStream is = Main.class.getResourceAsStream("/fonts/PressStart2P.ttf")) {
            if (is != null) {
                return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
            }
        } catch (Exception e) {
            System.err.println("Failed to load custom font: " + e.getMessage());
        }
        // Fallback to default font
        return new Font("Arial", Font.PLAIN, 24);
    }

}
