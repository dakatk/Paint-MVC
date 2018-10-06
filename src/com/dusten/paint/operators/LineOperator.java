package com.dusten.paint.operators;

import com.dusten.paint.primitives.Line;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * @author Dusten Knull
 *
 *
 */
public class LineOperator implements PaintOperator {

    private double lineWeight;
    private Paint paint;

    private double x0;
    private double y0;
    private double x1;
    private double y1;

    public LineOperator(Line line, double lineWeight) {

        this.lineWeight = lineWeight;
        this.paint = line.getPaint();

        this.x0 = line.getStartX();
        this.y0 = line.getStartY();

        this.x1 = line.getEndX();
        this.y1 = line.getEndY();
    }

    @Override
    public void draw(GraphicsContext context) {

        context.setStroke(this.paint);
        context.setLineWidth(this.lineWeight);
        context.strokeLine(this.x0, this.y0, this.x1, this.y1);
    }
}
