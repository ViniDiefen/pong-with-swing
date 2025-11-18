package br.com.vinidiefen.pong.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import br.com.vinidiefen.pong.constants.UIConstants;

/**
 * Factory class for creating styled buttons consistently across the UI
 */
public class ButtonFactory {
    
    private ButtonFactory() {
        throw new AssertionError("Cannot instantiate factory class");
    }
    
    /**
     * Creates a pause button
     */
    public static JButton createPauseButton() {
        return createGameButton("PAUSE", UIConstants.BUTTON_HOVER_COLOR);
    }
    
    /**
     * Creates a save button
     */
    public static JButton createSaveButton() {
        return createGameButton("SAVE", UIConstants.BUTTON_HOVER_COLOR);
    }
    
    /**
     * Creates a load button
     */
    public static JButton createLoadButton() {
        return createGameButton("LOAD", UIConstants.BUTTON_HOVER_BLUE);
    }
    
    /**
     * Creates a small game button (for pause, save, load in-game)
     */
    public static JButton createGameButton(String text, Color hoverColor) {
        return createButton(text, 
            new Dimension(UIConstants.SMALL_BUTTON_WIDTH, UIConstants.SMALL_BUTTON_HEIGHT), 
            UIConstants.GAME_BUTTON_FONT_SIZE, 
            hoverColor);
    }
    
    /**
     * Creates a menu button (for main menu)
     */
    public static JButton createMenuButton(String text) {
        return createButton(text, 
            new Dimension(UIConstants.MENU_BUTTON_WIDTH, UIConstants.MENU_BUTTON_HEIGHT), 
            null, 
            UIConstants.BUTTON_HOVER_COLOR);
    }
    
    /**
     * Core button creation method with common styling
     */
    private static JButton createButton(String text, Dimension size, Float fontSize, Color hoverColor) {
        JButton button = new JButton(text);
        
        // Set font
        Font buttonFont = (Font) UIManager.get("Label.font");
        if (buttonFont != null) {
            button.setFont(fontSize != null ? buttonFont.deriveFont(fontSize) : buttonFont);
        }
        
        // Button style
        button.setPreferredSize(size);
        button.setForeground(UIConstants.TEXT_COLOR);
        button.setBackground(UIConstants.OVERLAY_COLOR);
        button.setBorder(new LineBorder(UIConstants.TEXT_COLOR, 2));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(hoverColor);
                button.setBorder(new LineBorder(hoverColor, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(UIConstants.TEXT_COLOR);
                button.setBorder(new LineBorder(UIConstants.TEXT_COLOR, 2));
            }
        });
        
        return button;
    }
}
