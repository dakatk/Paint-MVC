package com.dusten.paint.operators;

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import jdk.internal.jline.internal.Nullable;

public class RectangleOperator implements PaintOperator {

    private Double lineWeight;
    private Paint paint;

    private double width;
    private double height;
    private double x;
    private double y;

    public RectangleOperator(@NotNull Rectangle rectangle, @Nullable Double lineWeight) {

        this.paint = rectangle.getStroke();
        this.lineWeight = lineWeight;

        this.width = rectangle.getWidth();
        this.height = rectangle.getHeight();

        this.x = rectangle.getX();
        this.y = rectangle.getY();

        if(this.width < 0.0) {

            this.width = Math.abs(this.width);
            this.x -= this.width;
        }

        if(this.height < 0.0) {

            this.height = Math.abs(this.height);
            this.y -= this.height;
        }
    }

    @Override
    public void draw(GraphicsContext context) {

        if(this.lineWeight != null) {

            context.setStroke(this.paint);
            context.setLineWidth(this.lineWeight);
            context.strokeRect(this.x, this.y, this.width, this.height);
        }
        else {

            context.setFill(this.paint);
            context.fillRect(this.x, this.y, this.width, this.height);
        }
    }
}
