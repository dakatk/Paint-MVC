package com.dusten.paint.primitives;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Paint;

/**
 * @author Dusten Knull
 *
 * Primitive line object for drawing with GraphicsContext
 */
public class Line {

    private Paint paint;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Line() {

        this.paint = null;

        this.startX = 0.0;
        this.startY = 0.0;

        this.endX = 0.0;
        this.endY = 0.0;
    }

    public void setPaint(@NotNull Paint paint) {
        this.paint = paint;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public double getStartX() {
        return this.startX;
    }

    public double getStartY() {
        return this.startY;
    }

    public double getEndX() {
        return this.endX;
    }

    public double getEndY() {
        return this.endY;
    }
}
