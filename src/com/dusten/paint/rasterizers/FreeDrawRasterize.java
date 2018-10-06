package com.dusten.paint.rasterizers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Dusten Knull
 */
public class FreeDrawRasterize {

    private ArrayList<Point> drawPoints;
    private GraphicsContext context;
    private Paint stroke;

    private double strokeWeight;
    private boolean brush;

    public FreeDrawRasterize() {
        this.drawPoints = new ArrayList<>();
    }

    /**
     *
     * @param context
     * @param stroke
     * @param brush
     * @param strokeWeight
     * @param x
     * @param y
     */
    public void resetStroke(GraphicsContext context, Paint stroke, boolean brush, double strokeWeight, double x, double y) {

        this.strokeWeight = strokeWeight;
        this.context = context;
        this.stroke = stroke;
        this.brush = brush;

        this.drawPoints.clear();
        this.drawPoints.add(new Point((int)x, (int)y));

        Paint currFill = this.context.getFill();
        this.context.setFill(stroke);

        double radius = this.strokeWeight / 2.0;

        if(this.brush) this.context.fillOval(x, y, this.strokeWeight, this.strokeWeight);
        else this.context.fillRect(x - radius, y - radius, this.strokeWeight, this.strokeWeight);

        context.setFill(currFill);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void drawNextStroke(double x, double y) {

        this.drawPoints.add(new Point((int)x, (int)y));
        this.renderBresenhamLine();
    }

    /**
     *
     */
    private void renderBresenhamLine() {

        Paint currFill = this.context.getFill();
        this.context.setFill(this.stroke);

        double radius = this.strokeWeight / 2.0;

        for(int i = 0; i < this.drawPoints.size() - 1; i ++) {

            if(i + 1 >= this.drawPoints.size())
                break;

            Point p0 = this.drawPoints.get(i);
            Point p1 = this.drawPoints.get(i + 1);

            double x0 = p0.getX(); // start x
            double y0 = p0.getY(); // start y

            double x1 = p1.getX(); // goal x
            double y1 = p1.getY(); // goal y

            double dx =  Math.abs(x1 - x0);
            double dy = -Math.abs(y1 - y0);

            double sx = x0 < x1 ? 1.0 : -1.0;
            double sy = y0 < y1 ? 1.0 : -1.0;
            double error = dx + dy;

            while(x0 != x1 || y0 != y1){

                if(this.brush) this.context.fillOval(x0, y0, this.strokeWeight, this.strokeWeight);
                else this.context.fillRect(x0 - radius, y0 - radius, this.strokeWeight, this.strokeWeight);

                double e2 = 2 * error;

                if(e2 > dy){

                    error += dy;
                    x0 += sx;
                }

                if(e2 < dx){

                    error += dx;
                    y0 += sy;
                }
            }
        }
        this.context.setFill(currFill);
    }

    public ArrayList<Point> getDrawPoints() {
        return this.drawPoints;
    }

    public Paint getStroke() {
        return this.stroke;
    }

    public boolean isBrush() {
        return this.brush;
    }
}
