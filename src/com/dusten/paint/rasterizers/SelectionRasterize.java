package com.dusten.paint.rasterizers;

import com.dusten.paint.primitives.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.awt.*;

/**
 * @author Dusten Knull
 */
public class SelectionRasterize {

    private static final double DASH_OFFSET = 5.0;

    private Rectangle selectedArea;
    private Point moveTo, clickAt;
    private Image sourceImage;

    private boolean hasSelected;

    public SelectionRasterize() {

        this.selectedArea = new Rectangle();
        this.selectedArea.setPaint(Color.GRAY);

        this.clickAt = new Point();
        this.moveTo = new Point();

        this.hasSelected = false;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void setSelectedRectangle(GraphicsContext context, double x, double y) {

        this.selectedArea.setX(x);
        this.selectedArea.setY(y);

        this.renderSelectedRectangle(context, x, y);
    }

    public void setMoveSelection(Image sourceImage, double x, double y) {

        if(!this.hasSelected)
            return;

        if(this.selectedArea.inside(x, y))
            this.clickAt.setLocation(x, y);
        else
            return;

        this.sourceImage = sourceImage;
        this.hasSelected = false;
    }

    /**
     *
     * @param context
     * @param x
     * @param y
     */
    public void renderSelectedRectangle(GraphicsContext context, double x, double y) {

        this.selectedArea.setWidth(Math.abs(x - this.selectedArea.getX()));
        this.selectedArea.setHeight(Math.abs(y - this.selectedArea.getY()));

        double renderX = this.selectedArea.getX();
        double renderY = this.selectedArea.getY();

        if(x < this.selectedArea.getX())
            renderX -= this.selectedArea.getWidth();

        if(y < this.selectedArea.getY())
            renderY -= this.selectedArea.getHeight();

        double[] lineDashes = context.getLineDashes();
        Paint currStroke = context.getStroke();

        context.setStroke(this.selectedArea.getPaint());
        context.setLineDashes(DASH_OFFSET);
        context.strokeRect(renderX, renderY, this.selectedArea.getWidth(), this.selectedArea.getHeight());

        context.setLineDashes(lineDashes);
        context.setStroke(currStroke);

        this.sourceImage = null;
        this.hasSelected = true;
    }

    /**
     *
     * @param context
     * @param x
     * @param y
     * @param cut
     */
    public void renderMoveSelection(GraphicsContext context, Paint background, double x, double y, boolean cut) {

        if(this.sourceImage == null)
            return;

        this.moveTo.setLocation(x - this.clickAt.getX(), y - this.clickAt.getY());

        double sourceX = this.selectedArea.getX();
        double sourceY = this.selectedArea.getY();
        double sourceW = this.selectedArea.getWidth();
        double sourceH = this.selectedArea.getHeight();

        if(cut) {

            Paint fill = context.getFill();

            context.setFill(background);
            context.fillRect(sourceX, sourceY, sourceW, sourceH);

            context.setFill(fill);
        }
        context.drawImage(this.sourceImage, sourceX, sourceY, sourceW, sourceH,
                this.moveTo.getX(), this.moveTo.getY(), sourceW, sourceH);
    }

    public Image getSourceImage() {
        return this.sourceImage;
    }

    public Rectangle getSelectedArea() {
        return this.selectedArea;
    }

    public Point getDest() {
        return this.moveTo;
    }

    public boolean hasSelectedArea() {
        return this.hasSelected;
    }
}
