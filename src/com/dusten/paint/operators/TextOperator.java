package com.dusten.paint.operators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.awt.*;

public class TextOperator implements PaintOperator {

    private Paint paint;
    private String text;
    private Font font;

    private double x;
    private double y;

    public TextOperator(Paint paint, String text, Font font, Point placed) {

        this.paint = paint;
        this.text = text;
        this.font = font;

        this.x = placed.getX();
        this.y = placed.getY();
    }

    @Override
    public void draw(GraphicsContext context) {

        context.setFont(this.font);
        context.setFill(this.paint);
        context.fillText(this.text, this.x, this.y);
    }
}
