package com.dusten.paint.operators;

import com.dusten.paint.components.primitives.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.Point;

public class MoveOperator implements PaintOperator {

    private Rectangle sourceRect;
    private Image sourceImg;
    private Point moveTo;

    private boolean cut;

    public MoveOperator(Image sourceImg, Rectangle sourceRect, Point moveTo, boolean cut) {

        this.sourceRect = sourceRect;
        this.sourceImg = sourceImg;
        this.moveTo = moveTo;
        this.cut = cut;
    }

    @Override
    public void draw(GraphicsContext context) {

        double sx = this.sourceRect.getX();
        double sy = this.sourceRect.getY();
        double sw = this.sourceRect.getWidth();
        double sh = this.sourceRect.getHeight();

        double dx = this.moveTo.getX();
        double dy = this.moveTo.getY();

        if(this.cut)  context.clearRect(sx, sy, sw, sh);
        context.drawImage(this.sourceImg, sx, sy, sw, sh, dx, dy, sw, sh);
    }
}
