package com.diefen.vini.renderers;

import java.awt.Color;
import java.awt.Graphics;

import com.diefen.vini.entities.Paddle;

/**
 * Handles rendering of paddle entities
 */
public class PaddleRenderer {

    /**
     * Render a paddle to the screen
     * 
     * @param g      Graphics context
     * @param paddle The paddle to render
     */
    public static void render(Graphics g, Paddle paddle) {
        g.setColor(Color.WHITE);
        g.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
    }

}
