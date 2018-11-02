package com.dusten.paint.rasterizers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;

public class TextRasterize {

    private StringBuilder builder;
    private Point textAt;
    private Paint paint;
    private Font font;

    private boolean textPlaced;

    public TextRasterize() {

        this.textAt = new Point();
        this.textPlaced = false;

    }

    public void setTextAt(GraphicsContext context, Paint paint, Font font, double x, double y) {

        this.builder = new StringBuilder();

        this.textPlaced = true;
        this.textAt.setLocation(x, y);

        this.paint = paint;
        this.font = font;

        Paint stroke = context.getStroke();

        context.setStroke(this.paint);
        context.strokeLine(x, y, x, y - this.font.getSize());
        context.setStroke(stroke);
    }

    public void addToText(GraphicsContext context, String character) {

        if(!this.textPlaced) return;
        if(character == null){

            if(this.builder.length() <= 0)
                return;

            this.builder.deleteCharAt(this.builder.length() - 1);
        }
        else this.builder.append(character);

        Paint stroke = context.getStroke();
        Paint fill = context.getFill();

        context.setStroke(this.paint);
        context.setFill(this.paint);
        context.setFont(this.font);

        Text text = new Text(this.builder.toString());
        text.setFont(this.font);

        context.fillText(this.builder.toString(), this.textAt.getX(), this.textAt.getY());
        context.strokeLine(this.textAt.getX() + text.getLayoutBounds().getWidth(), this.textAt.getY(),
                this.textAt.getX() + text.getLayoutBounds().getWidth(), this.textAt.getY() - this.font.getSize());

        context.setStroke(stroke);
        context.setFill(fill);
    }

    public void placeText() {
        this.textPlaced = false;
    }

    public boolean isTextPlaced() {
        return this.textPlaced;
    }

    public String getText() {
        return this.builder.toString();
    }

    public Point getPlacedAt() {
        return this.textAt;
    }
}
