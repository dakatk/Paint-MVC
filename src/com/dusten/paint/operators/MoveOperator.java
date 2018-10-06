package com.dusten.paint.operators;

import com.dusten.paint.primitives.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.Point;

public class MoveOperator implements PaintOperator {

    private Image sourceImg;
    private boolean cut;

    private double dx;
    private double dy;

    private double sx;
    private double sy;
    private double sw;
    private double sh;


    public MoveOperator(Image sourceImg, Rectangle sourceRect, Point moveTo, boolean cut) {

        this.sx = sourceRect.getX();
        this.sy = sourceRect.getY();
        this.sw = sourceRect.getWidth();
        this.sh = sourceRect.getHeight();

        this.dx = moveTo.getX();
        this.dy = moveTo.getY();

        this.sourceImg = sourceImg;
        this.cut = cut;
    }

    @Override
    public void draw(GraphicsContext context) {

        if(this.cut) context.fillRect(this.sx, this.sy, this.sw, this.sh);
        context.drawImage(this.sourceImg, this.sx, this.sy, this.sw,
                this.sh, this.dx, this.dy, this.sw, this.sh);
    }
}
