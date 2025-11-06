package br.com.vinidiefen.pong;

import java.awt.Font;
import java.io.InputStream;
import java.util.Optional;

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
        Optional<Font> customFontOptional = loadFont();
        customFontOptional.ifPresent(font -> UIManager.put("Label.font", font.deriveFont(24f)));
    }

    /**
     * Load custom font from resources
     */
    public static Optional<Font> loadFont() {
        try (InputStream is = Main.class.getResourceAsStream("/fonts/PressStart2P.ttf")) {
            return Optional.ofNullable(Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f));
        } catch (Exception e) {
            System.err.println("Failed to load custom font: " + e.getMessage());
        }
        // Fallback to default font
        return Optional.of(new Font("Arial", Font.PLAIN, 24));
    }

}
