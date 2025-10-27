package com.diefen.vini.renderers;

import java.awt.Color;
import java.awt.Graphics;

import com.diefen.vini.entities.Ball;

/**
 * Handles rendering of ball entity
 */
public class BallRenderer {

    /**
     * Render a ball to the screen
     * 
     * @param g    Graphics context
     * @param ball The ball to render
     */
    public static void render(Graphics g, Ball ball) {
        g.setColor(Color.WHITE);
        g.fillRect(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
    }

}
