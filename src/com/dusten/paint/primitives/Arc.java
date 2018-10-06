package com.dusten.paint.primitives;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Paint;

public class Arc {

    private Paint paint;

    private double angle;
    private double width;
    private double height;

    private double x;
    private double y;

    public Arc() {

        this.paint = null;

        this.angle = 0.0;
        this.width = 0.0;
        this.height = 0.0;

        this.x = 0.0;
        this.y = 0.0;
    }

    public void setEndPoint(double x, double y) {

        this.width = x - this.x;
        this.height = y - this.y;

        this.angle = Math.atan2(this.height, this.width);
        if(this.angle < 0.0)
            this.angle += 360.0;
    }

    public void setPaint(@NotNull Paint paint) {
        this.paint = paint;
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

    public double getAngle() {
        return this.angle;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }
}
