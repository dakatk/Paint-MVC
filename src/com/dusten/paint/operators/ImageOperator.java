package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * @author Dusten Knull
 *
 *
 */
public class ImageOperator implements PaintOperator {

    private Image image;

    private double canvasWidth;
    private double canvasHeight;

    public ImageOperator(Image image, double canvasWidth, double canvasHeight) {

        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.image = image;
    }

    @Override
    public void draw(GraphicsContext context) {

        context.clearRect(0.0, 0.0, this.canvasWidth, this.canvasHeight);
        context.drawImage(this.image, 0.0, 0.0, this.canvasWidth, this.canvasHeight);
    }
}
