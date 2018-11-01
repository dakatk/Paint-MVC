package com.dusten.paint.rasterizers;

import com.dusten.paint.primitives.Arc;
import com.dusten.paint.primitives.Ellipse;
import com.dusten.paint.primitives.Line;
import com.dusten.paint.primitives.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

public class ShapeRasterize {

    private Rectangle tempRectangle;
    private Ellipse tempEllipse;
    private Line tempLine;
    private Arc tempArc;

    public ShapeRasterize() {

        this.tempRectangle = new Rectangle();
        this.tempEllipse = new Ellipse();
        this.tempLine = new Line();
        this.tempArc = new Arc();
    }

    /**
     * Sets the current line's initial position to be at the
     * current mouse position
     *
     * @param paint The paint color to draw the line with
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void setLine(Paint paint, double x, double y) {

        this.tempLine.setPaint(paint);

        this.tempLine.setStartX(x);
        this.tempLine.setStartY(y);

        this.tempLine.setEndX(x);
        this.tempLine.setEndY(y);
    }

    /**
     *
     * @param paint The paint color to draw the arc with
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void setArc(Paint paint, double x, double y) {

        this.tempArc.setPaint(paint);

        this.tempArc.setX(x);
        this.tempArc.setY(y);

        this.tempArc.setEndPoint(x, y);
    }

    /**
     * Sets the current ellipse's initial position to be at the
     * current mouse position
     *
     * @param paint The paint color to draw the ellipse with
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void setEllipse(Paint paint, double x, double y) {

        this.tempEllipse.setPaint(paint);

        this.tempEllipse.setCenterX(x);
        this.tempEllipse.setCenterY(y);

        this.tempEllipse.setRadiusX(0.5);
        this.tempEllipse.setRadiusY(0.5);
    }

    /**
     * Sets the current rectangle's initial position to be at the
     * current mouse position
     *
     * @param paint The paint color to draw the rectangle with
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void setRectangle(Paint paint, double x, double y) {

        this.tempRectangle.setPosition(x, y);
        this.tempRectangle.setDimensions(1.0, 1.0);

        this.tempRectangle.setPaint(paint);
    }

    /**
     * Move current line on screen to a new endX and endY based on the
     * mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the line to
     * @param lineWeight Weight in pixels of the line to be drawn
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void renderLine(GraphicsContext context, double lineWeight, double x, double y) {

        this.tempLine.setEndX(x);
        this.tempLine.setEndY(y);

        double currWeight = context.getLineWidth();
        Paint currStroke = context.getStroke();

        context.setLineWidth(lineWeight);
        context.setStroke(this.tempLine.getPaint());
        context.strokeLine(this.tempLine.getStartX(), this.tempLine.getStartY(),
                this.tempLine.getEndX(), this.tempLine.getEndY());

        context.setLineWidth(currWeight);
        context.setStroke(currStroke);
    }

    /**
     * Move current arc on screen to a new endX and endY based on the
     * mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the arc to
     * @param lineWeight Weight in pixels of the arc to be drawn
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void renderArc(GraphicsContext context, double lineWeight, double x, double y) {

        this.tempArc.setEndPoint(x, y);

        double renderX = this.tempArc.getX();
        double renderY = this.tempArc.getY();

        if(this.tempArc.getWidth() < 0.0)
            renderX += this.tempArc.getWidth();

        if(this.tempArc.getHeight() < 0.0)
            renderY += this.tempArc.getHeight();

        double currWeight = context.getLineWidth();
        Paint currStroke = context.getStroke();

        context.setLineWidth(lineWeight);
        context.setStroke(this.tempArc.getPaint());
        context.strokeArc(renderX, renderY, Math.abs(this.tempArc.getWidth()),
                Math.abs(this.tempArc.getHeight()), this.tempArc.getAngle(), 180.0, ArcType.OPEN);

        context.setLineWidth(currWeight);
        context.setStroke(currStroke);
    }

    /**
     * Move current ellipse on screen to a new centerX and centerY
     * based on the mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the ellipse to
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void renderFillEllipse(GraphicsContext context, double x, double y) {
        this.renderEllipse(context, null, x, y);
    }

    /**
     * Move current ellipse on screen to a new centerX and centerY
     * based on the mouse position, then redraw
     *
     * @param context The current GraphicsContext to render the ellipse to
     * @param lineWeight Weight in pixels of the ellipse to be drawn
     * @param x Current mouse pointer 'x' position on canvas
     * @param y Current mouse pointer 'y' position on canvas
     */
    public void renderDrawEllipse(GraphicsContext context, double lineWeight, double x, double y) {
        this.renderEllipse(context, lineWeight, x, y);
    }

    private void renderEllipse(GraphicsContext context, Double lineWeight, double x, double y) {

        this.tempEllipse.setRadiusX(x - this.tempEllipse.getCenterX());
        this.tempEllipse.setRadiusY(y - this.tempEllipse.getCenterY());

        double renderX = this.tempEllipse.getCenterX();
        double renderY = this.tempEllipse.getCenterY();

        if(this.tempEllipse.getRadiusX() < 0.0)
            renderX += this.tempEllipse.getRadiusX() * 2.0;

        if(this.tempEllipse.getRadiusY() < 0.0)
            renderY += this.tempEllipse.getRadiusY() * 2.0;

        if(lineWeight != null) {

            double currWeight = context.getLineWidth();
            Paint currStroke = context.getStroke();

            context.setStroke(this.tempEllipse.getPaint());
            context.setLineWidth(lineWeight);
            context.strokeOval(renderX, renderY, this.tempEllipse.getRadiusX(), this.tempEllipse.getRadiusY());

            context.setLineWidth(currWeight);
            context.setFill(currStroke);

        } else {

            Paint currFill = context.getFill();

            context.setFill(this.tempEllipse.getPaint());
            context.fillOval(renderX, renderY, this.tempEllipse.getRadiusX(), this.tempEllipse.getRadiusY());

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

    public Arc getArc() {
        return this.tempArc;
    }

    public Line getLine() {
        return this.tempLine;
    }

    public Ellipse getEllipse() {
        return this.tempEllipse;
    }

    public Rectangle getRectangle() {
        return this.tempRectangle;
    }
}
