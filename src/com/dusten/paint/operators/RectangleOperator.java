package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleOperator implements PaintOperator {

    private Paint fill;
    private boolean outline;
    private double lineWeight;

    private double width;
    private double height;
    private double x;
    private double y;

    public RectangleOperator(Rectangle rectangle, double lineWeight, boolean outline) {

        this.width = rectangle.getWidth();
        this.height = rectangle.getHeight();

        this.fill = rectangle.getStroke();

        this.x = rectangle.getX();
        this.y = rectangle.getY();

        this.outline = outline;
        this.lineWeight = lineWeight;

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

        if(this.outline) {

            context.setLineWidth(this.lineWeight);
            context.setStroke(this.fill);
            context.strokeRect(this.x, this.y, this.width, this.height);
        }
        else {

            context.setFill(this.fill);
            context.fillRect(this.x, this.y, this.width, this.height);
        }
    }
}
