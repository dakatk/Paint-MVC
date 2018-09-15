package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;

/**
 * @author Dusten Knull
 *
 *
 */
public interface PaintOperator {

    /**
     *
     *
     * @param context
     */
    void draw(GraphicsContext context);
}
