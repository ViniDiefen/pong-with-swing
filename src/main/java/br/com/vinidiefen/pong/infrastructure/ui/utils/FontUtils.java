package br.com.vinidiefen.pong.infrastructure.ui.utils;

import java.awt.Font;
import javax.swing.UIManager;

/**
 * Utility class for font operations
 */
public final class FontUtils {
    
    private FontUtils() {
        throw new AssertionError("Cannot instantiate utility class");
    }
    
    /**
     * Gets the default font from UIManager or a fallback
     * 
     * @return The default font
     */
    public static Font getDefaultFont() {
        Font font = (Font) UIManager.get("Label.font");
        return font != null ? font : new Font("Arial", Font.PLAIN, 12);
    }
    
    /**
     * Gets the default font with a specific size
     * 
     * @param size The font size
     * @return The default font with the specified size
     */
    public static Font getDefaultFont(float size) {
        return getDefaultFont().deriveFont(size);
    }
    
    /**
     * Gets the default font with a specific style and size
     * 
     * @param style The font style (e.g., Font.BOLD)
     * @param size The font size
     * @return The default font with the specified style and size
     */
    public static Font getDefaultFont(int style, float size) {
        return getDefaultFont().deriveFont(style, size);
    }
}
