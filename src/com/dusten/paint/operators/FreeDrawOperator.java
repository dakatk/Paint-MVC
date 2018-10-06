package com.dusten.paint.operators;

import com.dusten.paint.operators.tasks.FreeDraw;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;

public class FreeDrawOperator implements PaintOperator {

    private Task<Void> subroutine;
    private WritableImage result;

    public FreeDrawOperator(Paint paint, ArrayList<Point> drawPoints, double lineWeight,
                            boolean brush, double canvasWidth, double canvasHeight) {

        this.result = new WritableImage((int)canvasWidth, (int)canvasHeight);
        this.subroutine = new FreeDraw(paint, drawPoints, this.result, lineWeight, brush);

        new Thread(this.subroutine).start();
    }

    @Override
    public void draw(GraphicsContext context) {

        context.drawImage(this.result, 0.0, 0.0, this.result.getWidth(), this.result.getHeight());

        this.subroutine.setOnSucceeded(event ->
                context.drawImage(this.result, 0.0, 0.0, this.result.getWidth(), this.result.getHeight())
        );
    }
}
