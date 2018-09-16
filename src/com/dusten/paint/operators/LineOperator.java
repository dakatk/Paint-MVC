package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

/**
 * @author Dusten Knull
 *
 *
 */
public class LineOperator implements PaintOperator {

    private double lineWeight;
    private Paint fill;

    private double x0;
    private double y0;
    private double x1;
    private double y1;

    public LineOperator(Line line, double lineWeight) {

        this.lineWeight = lineWeight;
        this.fill = line.getStroke();
        this.x0 = line.getStartX();
        this.y0 = line.getStartY();
        this.x1 = line.getEndX();
        this.y1 = line.getEndY();
    }

    @Override
    public void draw(GraphicsContext context) {

        double currWeight = context.getLineWidth();

        context.setLineWidth(this.lineWeight);
        context.setStroke(this.fill);
        context.strokeLine(this.x0, this.y0, this.x1, this.y1);

        context.setLineWidth(currWeight);
    }
}
