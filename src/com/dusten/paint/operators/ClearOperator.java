package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class ClearOperator implements PaintOperator {

    private Paint paint;

    private double canvasWidth;
    private double canvasHeight;

    public ClearOperator(Paint paint, double canvasWidth, double canvasHeight) {

        this.paint = paint;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    @Override
    public void draw(GraphicsContext context) {

        context.setFill(this.paint);
        context.clearRect(0.0, 0.0, this.canvasWidth, this.canvasHeight);
    }
}
