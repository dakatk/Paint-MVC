package com.dusten.paint.operators;

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import jdk.internal.jline.internal.Nullable;

public class EllipseOperator implements PaintOperator {

    private Double lineWeight;
    private Paint paint;

    private double radiusX;
    private double radiusY;
    private double x;
    private double y;


    public EllipseOperator(@NotNull Circle ellipse, @Nullable Double lineWeight) {

        this.paint = ellipse.getStroke();
        this.lineWeight = lineWeight;

        this.radiusX = ellipse.getScaleX();
        this.radiusY = ellipse.getScaleY();

        this.x = ellipse.getCenterX();
        this.y = ellipse.getCenterY();
    }

    @Override
    public void draw(GraphicsContext context) {

        if(lineWeight != null) {

            context.setStroke(this.paint);
            context.setLineWidth(this.lineWeight);
            context.strokeOval(this.x, this.y, this.radiusX, this.radiusY);
        }
        else {

            context.setFill(this.paint);
            context.fillOval(this.x, this.y, this.radiusX, this.radiusY);
        }
    }
}
