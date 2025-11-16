package br.com.vinidiefen.pong.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import br.com.vinidiefen.pong.components.FieldLine;

public class FieldLineRenderer {
    
    protected float[] dashPattern = { 10f, 10f };
    
    public static void render(Graphics g, FieldLine line) {
        int middleScreenX = line.getParent().getWidth() / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(line.getWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, line.getDashPattern(), 0f));
        g2d.drawLine(middleScreenX, 0, middleScreenX, line.getParent().getHeight());
    }

}
