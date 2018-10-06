package com.dusten.paint.operators;

import com.dusten.paint.primitives.Arc;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

public class ArcOperator implements PaintOperator {

    private Paint paint;

    private double lineWeight;
    private double angle;

    private double x;
    private double y;
    private double w;
    private double h;

    public ArcOperator(Arc arc, double lineWeight) {

        this.paint = arc.getPaint();

        this.lineWeight = lineWeight;
        this.angle = arc.getAngle();

        this.x = arc.getX();
        this.y = arc.getY();
        this.w = arc.getWidth();
        this.h = arc.getHeight();

        if(this.w < 0) {

            this.w = Math.abs(this.w);
            this.x -= this.w;
        }

        if(this.h < 0) {

            this.h = Math.abs(this.h);
            this.y -= this.h;
        }
    }

    @Override
    public void draw(GraphicsContext context) {

        context.setLineWidth(this.lineWeight);
        context.setStroke(this.paint);
        context.strokeArc(this.x, this.y, this.w, this.h, this.angle, 180.0, ArcType.OPEN);
    }
}
