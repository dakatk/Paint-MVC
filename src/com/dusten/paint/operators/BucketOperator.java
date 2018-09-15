package com.dusten.paint.operators;

import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;

/**
 * @author Dusten Knull
 *
 *
 */
public class BucketOperator implements PaintOperator {

    private Task<Void> subroutine;
    private WritableImage image;

    private double canvasWidth;
    private double canvasHeight;

    public BucketOperator(WritableImage image, Paint fill, double epsilon, double mouseX,
                          double mouseY, double canvasWidth, double canvadHeight) {

        this.canvasHeight = canvadHeight;
        this.canvasWidth = canvasWidth;
        this.image = image;

        this.subroutine = new BucketFill(this.image, fill, epsilon, mouseX, mouseY);
        new Thread(this.subroutine).start();
    }

    @Override
    public void draw(GraphicsContext context) {

        context.drawImage(this.image, 0.0, 0.0, this.canvasWidth, this.canvasHeight);

        this.subroutine.setOnSucceeded(event -> {
            context.drawImage(this.image, 0.0, 0.0, this.canvasWidth, this.canvasHeight);
        });
    }
}
