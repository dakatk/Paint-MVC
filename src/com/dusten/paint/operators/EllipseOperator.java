package com.dusten.paint.operators;

import com.dusten.paint.primitives.Ellipse;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class EllipseOperator implements PaintOperator {

    private Double lineWeight;
    private Paint paint;

    private double radiusX;
    private double radiusY;
    private double x;
    private double y;


    public EllipseOperator(@NotNull Ellipse ellipse, @Nullable Double lineWeight) {

        this.paint = ellipse.getPaint();
        this.lineWeight = lineWeight;

        this.radiusX = ellipse.getRadiusX();
        this.radiusY = ellipse.getRadiusY();

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
