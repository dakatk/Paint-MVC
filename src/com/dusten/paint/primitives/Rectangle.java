package com.dusten.paint.primitives;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Paint;

/**
 * @author Dusten Knull
 *
 * Primitive rectangle object for drawing with GraphicsContext
 */
public class Rectangle {

    private Paint paint;

    private double width;
    private double height;
    private double x;
    private double y;

    public Rectangle(double x, double y, double width, double height) {

        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public void setDimensions(double width, double height) {

        this.width = width;
        this.height = height;
    }

    public void setPosition(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public boolean inside(double x, double y) {

        return this.width > 0 && this.height > 0 &&
                x >= this.x && x < this.x + this.width &&
                x >= this.y && y < this.y + this.height;
    }

    public void setPaint(@NotNull Paint paint) {
        this.paint = paint;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
