package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;

/**
 * @author Dusten Knull
 *
 * Interface that serves as a template for all operators that are registerd
 * as image edits (e.g., the Line Tool, Paint Bucket Tool, etc.)
 */
public interface PaintOperator {

    /**
     * Describes how the current edit is drawn to the given GraphicsContext
     *
     * @param context the GraphicsContext of the current canvas component
     */
    void draw(GraphicsContext context);
}
