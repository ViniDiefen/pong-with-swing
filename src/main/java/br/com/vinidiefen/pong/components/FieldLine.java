package br.com.vinidiefen.pong.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class FieldLine extends GameObject {

    protected float[] dashPattern = { 10f, 10f };

    public FieldLine() {
        super();
        width = 3;
    }

    @Override
    void update() {
        // Field line does not need to update
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(Graphics g) {
        int middleScreenX = getParent().getWidth() / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f));
        g2d.drawLine(middleScreenX, 0, middleScreenX, getParent().getHeight());
    }

    public float[] getDashPattern() {
        return dashPattern;
    }

}
