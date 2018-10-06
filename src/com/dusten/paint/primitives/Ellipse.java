package com.dusten.paint.primitives;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Paint;

public class Ellipse {

    private Paint paint;

    private double centerX;
    private double centerY;
    private double radiusX;
    private double radiusY;

    public Ellipse() {

        this.paint = null;

        this.centerX = 0.0;
        this.centerY = 0.0;

        this.radiusX = 0.0;
        this.radiusY = 0.0;
    }

    public void setPaint(@NotNull Paint paint) {
        this.paint = paint;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public void setRadiusX(double radiusX) {
        this.radiusX = radiusX;
    }

    public void setRadiusY(double radiusY) {
        this.radiusY = radiusY;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public double getCenterX() {
        return this.centerX;
    }

    public double getCenterY() {
        return this.centerY;
    }

    public double getRadiusX() {
        return this.radiusX;
    }

    public double getRadiusY() {
        return this.radiusY;
    }
}
