package com.dusten.paint.operators;

import com.dusten.paint.operators.tasks.BucketFill;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;

/**
 * @author Dusten Knull
 *
 *
 */
public class BucketOperator implements PaintOperator {

    private Task<Void> subroutine;
    private WritableImage result;

    private double canvasWidth;
    private double canvasHeight;

    public BucketOperator(Image canvasImage, Paint paint, double epsilon, double mouseX,
                          double mouseY, double canvasWidth, double canvadHeight) {

        this.canvasHeight = canvadHeight;
        this.canvasWidth = canvasWidth;

        this.result = new WritableImage((int)canvasImage.getWidth(), (int)canvasImage.getHeight());
        this.subroutine = new BucketFill(this.result, canvasImage, paint, epsilon, mouseX, mouseY);

       // synchronized(this){
            new Thread(this.subroutine).start();
        //}
    }

    @Override
    public void draw(GraphicsContext context) {

        context.drawImage(this.result, 0.0, 0.0, this.canvasWidth, this.canvasHeight);

        this.subroutine.setOnSucceeded(event ->
            context.drawImage(this.result, 0.0, 0.0, this.canvasWidth, this.canvasHeight)
        );
    }
}
