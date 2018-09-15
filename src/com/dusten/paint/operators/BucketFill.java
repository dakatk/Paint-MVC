package com.dusten.paint.operators;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.Stack;

/**
 * @author Dusten Knull
 *
 *
 */
class BucketFill extends Task<Void> {

    private static final double EPSILON = 0.15;

    private WritableImage canvasImage;
    private Point clickedPoint;
    private Color fillColor;

    BucketFill(WritableImage canvasImage, Paint fillColor, double mouseX, double mouseY) {

        this.canvasImage = canvasImage;
        this.fillColor = (Color)fillColor;
        this.clickedPoint = new Point((int)mouseX, (int)mouseY);
    }

    @Override
    protected Void call() {

        Stack<Point> stack = new Stack<>();

        PixelReader pixelReader = this.canvasImage.getPixelReader();
        PixelWriter pixelWriter = this.canvasImage.getPixelWriter();

        int clickX = (int)this.clickedPoint.getX();
        int clickY = (int)this.clickedPoint.getY();

        Color targetColor = pixelReader.getColor(clickX, clickY);

        if(this.fillColor.equals(targetColor))
            return null;

        stack.push(this.clickedPoint);
        while(!stack.empty()) {

            Point pixel = stack.pop();

            int pixelX = (int)pixel.getX();
            int pixelY = (int)pixel.getY();

            Color pixelColor = pixelReader.getColor(pixelX, pixelY);
            if(this.isAlreadyFilled(pixelColor, targetColor))
                continue;

            pixelWriter.setColor(pixelX, pixelY, this.fillColor);

            Point[] nextPoints = {
                    pixel,                             // current
                    new Point(pixelX - 1, pixelY - 1), // bottom left
                    new Point(pixelX - 1, pixelY),     // left
                    new Point(pixelX - 1, pixelY + 1), // top left
                    new Point(pixelX, pixelY + 1),     // top
                    new Point(pixelX + 1, pixelY + 1), // top right
                    new Point(pixelX + 1, pixelY),     // right
                    new Point(pixelX + 1, pixelY - 1), // top left
                    new Point(pixelX, pixelY - 1),     // left
            };

            for(Point p : nextPoints) {

                if(this.isInBounds(p, this.canvasImage))
                    stack.push(p);
            }
        }
        return null;
    }

    /**
     *
     *
     * @param point
     * @param image
     * @return
     */
    private boolean isInBounds(Point point, Image image) {

        return point.getX() > 0.0 && point.getX() < image.getWidth() &&
                point.getY() > 0.0 && point.getY() < image.getHeight();
    }

    /**
     *
     *
     * @param readColor
     * @param colorToFill
     * @return
     */
    private boolean isAlreadyFilled(Color readColor, Color colorToFill) {
        return !this.withinTolerance(readColor, colorToFill);
    }

    /**
     *
     *
     * @param a
     * @param b
     * @return
     */
    private boolean withinTolerance(Color a, Color b) {

        return this.withinTolerance(a.getRed(), b.getRed()) &&
                this.withinTolerance(a.getGreen(), b.getGreen()) &&
                this.withinTolerance(a.getBlue(), b.getBlue());
    }

    /**
     *
     *
     * @param a
     * @param b
     * @return
     */
    private boolean withinTolerance(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
}
