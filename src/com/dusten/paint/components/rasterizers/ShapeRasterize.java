package com.dusten.paint.components.rasterizers;

import com.dusten.paint.components.primitives.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ShapeRasterize {

    private Rectangle tempRectangle;
    private Circle tempEllipse;
    private Line tempLine;

    public ShapeRasterize() {

        this.tempRectangle = new Rectangle();
        this.tempEllipse = new Circle();
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
    public void setLine(Paint stroke, double x, double y) {

        this.tempLine.setStartX(x);
        this.tempLine.setStartY(y);

        this.tempLine.setEndX(x);
        this.tempLine.setEndY(y);

        this.tempLine.setStroke(stroke);
    }

    /**
     *
     * @param stroke
     * @param x
     * @param y
     */
    public void setEllipse(Paint stroke, double x, double y) {

        this.tempEllipse.setCenterX(x);
        this.tempEllipse.setCenterY(y);

        this.tempEllipse.setScaleX(0.5);
        this.tempEllipse.setScaleY(0.5);

        this.tempEllipse.setStroke(stroke);
    }

    /**
     *
     * @param stroke
     * @param x
     * @param y
     */
    public void setRectangle(Paint stroke, double x, double y) {

        this.tempRectangle.setPosition(x, y);
        this.tempRectangle.setDimensions(1.0, 1.0);

        this.tempRectangle.setPaint(stroke);
    }

    /**
     * Move current line on screen to a new endX and endY based on the
     * mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the line to
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void renderLine(GraphicsContext context, double lineWeight, double x, double y) {

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

    public void renderFillEllipse(GraphicsContext context, double x, double y) {
        this.renderEllipse(context, null, x, y);
    }

    public void renderDrawEllipse(GraphicsContext context, double lineWeight, double x, double y) {
        this.renderEllipse(context, lineWeight, x, y);
    }

    /**
     *
     * @param context
     * @param lineWeight
     * @param x
     * @param y
     */
    private void renderEllipse(GraphicsContext context, Double lineWeight, double x, double y) {

        this.tempEllipse.setScaleX(Math.abs(x - this.tempEllipse.getCenterX()));
        this.tempEllipse.setScaleY(Math.abs(y - this.tempEllipse.getCenterY()));

        if(lineWeight != null) {

            double currWeight = context.getLineWidth();
            Paint currStroke = context.getStroke();

            context.setStroke(this.tempEllipse.getStroke());
            context.setLineWidth(lineWeight);
            context.strokeOval(this.tempEllipse.getCenterX(), this.tempEllipse.getCenterY(),
                    this.tempEllipse.getScaleX(), this.tempEllipse.getScaleY());

            context.setLineWidth(currWeight);
            context.setFill(currStroke);

        } else {

            Paint currFill = context.getFill();

            context.setFill(this.tempEllipse.getStroke());
            context.fillOval(this.tempEllipse.getCenterX(), this.tempEllipse.getCenterY(),
                    this.tempEllipse.getScaleX(), this.tempEllipse.getScaleY());

            context.setFill(currFill);
        }
    }

    /**
     *
     * @param context
     * @param x
     * @param y
     */
    public void renderFillRectangle(GraphicsContext context, double x, double y) {
        this.renderRectangle(context, null, x, y);
    }

    /**
     *
     * @param context
     * @param lineWeight
     * @param x
     * @param y
     */
    public void renderDrawRectangle(GraphicsContext context, double lineWeight, double x, double y) {
        this.renderRectangle(context, lineWeight, x, y);
    }

    /**
     * Generic render function for filling or drawing a rectangle
     *
     * @param context
     * @param lineWeight
     * @param x
     * @param y
     */
    private void renderRectangle(GraphicsContext context, Double lineWeight, double x, double y) {

        this.tempRectangle.setWidth(x - this.tempRectangle.getX());
        this.tempRectangle.setHeight(y - this.tempRectangle.getY());

        double renderX = this.tempRectangle.getX();
        double renderY = this.tempRectangle.getY();

        if(this.tempRectangle.getWidth() < 0.0)
            renderX += this.tempRectangle.getWidth();

        if(this.tempRectangle.getHeight() < 0.0)
            renderY += this.tempRectangle.getHeight();

        if(lineWeight != null) {

            double currWeight = context.getLineWidth();
            Paint currStroke = context.getStroke();

            context.setLineWidth(lineWeight);
            context.setStroke(this.tempRectangle.getPaint());
            context.strokeRect(renderX, renderY, Math.abs(this.tempRectangle.getWidth()), Math.abs(this.tempRectangle.getHeight()));

            context.setLineWidth(currWeight);
            context.setStroke(currStroke);
        }
        else {

            Paint currFill = context.getFill();

            context.setFill(this.tempRectangle.getPaint());
            context.fillRect(renderX, renderY, Math.abs(this.tempRectangle.getWidth()), Math.abs(this.tempRectangle.getHeight()));

            context.setFill(currFill);
        }
    }

    public Line getLine() {
        return this.tempLine;
    }

    public Circle getEllipse() {
        return this.tempEllipse;
    }

    public Rectangle getRectangle() {
        return this.tempRectangle;
    }
}
