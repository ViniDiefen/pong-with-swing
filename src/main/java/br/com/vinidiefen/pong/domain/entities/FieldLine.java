package br.com.vinidiefen.pong.domain.entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import br.com.vinidiefen.pong.constants.GameConstants;
import br.com.vinidiefen.pong.constants.UIConstants;

public class FieldLine extends GameObject {

    protected float[] dashPattern = UIConstants.FIELD_LINE_DASH_PATTERN;

    public FieldLine() {
        super();
        width = GameConstants.FIELD_LINE_WIDTH;
    }

    @Override
    public void update() {
        // Field line is static
    }

    @Override
    public void render(Graphics g) {
        if (getParent() == null) {
            return;
        }

        int middleScreenX = getParent().getWidth() / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f));
        g2d.drawLine(middleScreenX, 0, middleScreenX, getParent().getHeight());
    }

}
