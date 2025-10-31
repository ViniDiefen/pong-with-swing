package br.com.vinidiefen.pong.entities;

import java.awt.Component;
import java.awt.Graphics;

import br.com.vinidiefen.pong.renderers.FieldLineRenderer;

public class FieldLine extends GameObject {

    protected float[] dashPattern = { 10f, 10f };

    public FieldLine(Component parent) {
        super(parent, parent.getWidth() / 2, 0, 3, 0);
    }

    @Override
    void update() {
        // Field line does not need to update
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void render(Graphics g) {
        FieldLineRenderer.render(g, this);
    }

    public float[] getDashPattern() {
        return dashPattern;
    }

}
