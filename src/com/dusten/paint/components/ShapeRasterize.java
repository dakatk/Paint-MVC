package com.dusten.paint.components;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

class ShapeRasterize {

    private Rectangle tempRectangle;
    private Line tempLine;

    ShapeRasterize() {

        this.tempRectangle = new Rectangle();
        this.tempLine = new Line();
    }

    /**
     * Sets the current line's initial position to be at the
     * current mouse position
     *
     * @param stroke The stroke color to draw the line with
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    void setLine(Paint stroke, double x, double y) {

        this.tempLine.setStartX(x);
        this.tempLine.setStartY(y);

        this.tempLine.setEndX(x);
        this.tempLine.setEndY(y);

        this.tempLine.setStroke(stroke);
    }

    void setRectangle(Paint stroke, double x, double y) {

        this.tempRectangle.setX(x);
        this.tempRectangle.setY(y);

        this.tempRectangle.setWidth(1.0);
        this.tempRectangle.setHeight(1.0);

        this.tempRectangle.setStroke(stroke);
    }

    /**
     * Move current line on screen to a new endX and endY based on the
     * mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the line to
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    void renderLine(GraphicsContext context, double lineWeight, double x, double y) {

        this.tempLine.setEndX(x);
        this.tempLine.setEndY(y);

        double currWeight = context.getLineWidth();
        Paint currStroke = context.getStroke();

        context.setLineWidth(lineWeight);
        context.setStroke(this.tempLine.getStroke());
        context.strokeLine(this.tempLine.getStartX(), this.tempLine.getStartY(),
                this.tempLine.getEndX(), this.tempLine.getEndY());

        context.setLineWidth(currWeight);
        context.setStroke(currStroke);
    }

    void renderFillRectangle(GraphicsContext context, double x, double y) {
        this.renderRectangle(context, false, x, y);
    }

    void renderDrawRectangle(GraphicsContext context, double x, double y) {
        this.renderRectangle(context, true, x, y);
    }

    private void renderRectangle(GraphicsContext context, boolean outline, double x, double y) {

        this.tempRectangle.setWidth(x - this.tempRectangle.getX());
        this.tempRectangle.setHeight(y - this.tempRectangle.getY());

        double renderX = this.tempRectangle.getX();
        double renderY = this.tempRectangle.getY();

        if(this.tempRectangle.getWidth() < 0.0)
            renderX += this.tempRectangle.getWidth();

        if(this.tempRectangle.getHeight() < 0.0)
            renderY += this.tempRectangle.getHeight();

        Paint currFill = context.getFill();

        context.setFill(this.tempRectangle.getStroke());

        if(outline)
            context.strokeRect(renderX, renderY, Math.abs(this.tempRectangle.getWidth()), Math.abs(this.tempRectangle.getHeight()));
        else
            context.fillRect(renderX, renderY, Math.abs(this.tempRectangle.getWidth()), Math.abs(this.tempRectangle.getHeight()));

        context.setFill(currFill);
    }

    Line getLine() {
        return this.tempLine;
    }

    Rectangle getRectangle() {
        return this.tempRectangle;
    }
}
