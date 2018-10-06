package com.dusten.paint.operators.tasks;

import javafx.concurrent.Task;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;

public class FreeDraw extends Task<Void> {

    private ArrayList<Point> drawPoints;

    private WritableImage resultImage;
    private Color drawColor;

    private double strokeWeight;
    private boolean brush;

    public FreeDraw(Paint paint, ArrayList<Point> drawPoints, WritableImage resultImage, double strokeWeight, boolean brush) {

        this.drawColor = (Color)paint;
        this.drawPoints = drawPoints;
        this.resultImage = resultImage;

        this.brush = brush;
        this.strokeWeight = strokeWeight;
    }

    @Override
    protected Void call() {

        PixelWriter writer = this.resultImage.getPixelWriter();
        Point start = this.drawPoints.get(0);

        if(this.brush) this.drawCircle(writer, start.getX(), start.getY(), this.strokeWeight);
        else this.drawSquare(writer, start.getX(), start.getY(), this.strokeWeight);

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

            while(x0 != x1 || y0 != y1) {

                if(this.brush) this.drawCircle(writer, x0, y0, this.strokeWeight);
                else this.drawSquare(writer, x0, y0, this.strokeWeight);

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
        return null;
    }

    private void drawSquare(PixelWriter writer, double x, double y, double size) {

        x -= size / 2.0;
        y -= size / 2.0;

        for(int i = (int)x; i < (int)Math.floor(x + size); i ++) {

            if(i < 0 || i >= this.resultImage.getWidth()) continue;
            for(int j = (int)y; j < (int)(y + size); j ++) {

                if(j < 0 || j >= this.resultImage.getHeight()) continue;
                writer.setColor(i, j, this.drawColor);
            }
        }
    }

    private void drawCircle(PixelWriter writer, double x, double y, double size) {

        double centerX = x + (size / 2.0);
        double centerY = y + (size / 2.0);

        for(int i = (int)x; i <= (int)Math.floor(x + size); i ++) {

            if(i < 0 || i >= this.resultImage.getWidth()) continue;
            for(int j = (int)y; j <= (int)(y + size); j ++) {

                if(j < 0 || j >= this.resultImage.getHeight()) continue;

                double dist = Math.sqrt(Math.pow(i - centerX, 2.0) + Math.pow(j - centerY, 2.0));
                if(dist < this.strokeWeight / 2.0)
                    writer.setColor(i, j, this.drawColor);
            }
        }
    }
}
