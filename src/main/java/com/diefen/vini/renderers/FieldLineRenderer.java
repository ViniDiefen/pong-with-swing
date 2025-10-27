package com.diefen.vini.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.diefen.vini.entities.FieldLine;

public class FieldLineRenderer {
    
    public static void render(Graphics g, FieldLine line) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(line.getWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, line.getDashPattern(), 0f));
        g2d.drawLine(line.getX(), line.getY(), line.getX(), line.getParent().getHeight());
    }

}
