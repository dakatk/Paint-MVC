package com.dusten.paint.components;

import com.dusten.paint.components.rasterizers.FreeDrawRasterize;
import com.dusten.paint.components.rasterizers.ShapeRasterize;
import com.dusten.paint.controllers.ImageController;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.operators.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dusten Knull
 */
public class DrawableCanvas extends Canvas {

    public static final double DEFAULT_FILL_TOLERANCE = 0.1;
    public static final double DEFAULT_LINE_WEIGHT = 2.0;

    private FreeDrawRasterize freeDrawRasterize;
    private ShapeRasterize shapeRasterize;

    private ImageController imageController;
    private GraphicsContext context;
    private ToolsEnum toolType;

    private List<PaintOperator> editHistory;
    private int editIndex;

    private double fillTolerance;
    private double lineWeight;

    public DrawableCanvas() {

        this.fillTolerance = DEFAULT_FILL_TOLERANCE;
        this.lineWeight = DEFAULT_LINE_WEIGHT;

        this.editHistory = new ArrayList<>();
        this.editIndex = -1;

        this.freeDrawRasterize = new FreeDrawRasterize();
        this.shapeRasterize = new ShapeRasterize();
        this.toolType = null;

        this.context = this.getGraphicsContext2D();
        this.context.setFill(Color.WHITE);
        this.context.setStroke(Color.WHITE);

        this.createAndSetEvents();
    }

    /**
     * Initial setup for all mouse events handled by the canvas
     */
    private void createAndSetEvents() {

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {

            if(this.toolType == null)
                event.consume();

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.redraw();
                    this.shapeRasterize.renderLine(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case RECTANGLE_FILL: // ordinal 4
                    this.redraw();
                    this.shapeRasterize.renderFillRectangle(this.context, event.getX(), event.getY());
                    break;

                case RECTANGLE_DRAW: // ordinal 5
                    this.redraw();
                    this.shapeRasterize.renderDrawRectangle(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case ELLIPSE_FILL: // ordinal 6
                    this.redraw();
                    this.shapeRasterize.renderFillEllipse(this.context, event.getX(), event.getY());
                    break;

                case ELLIPSE_DRAW: // ordinal 7
                    this.redraw();
                    this.shapeRasterize.renderDrawEllipse(this.context, this.lineWeight, event.getX(), event.getY());
                    break;

                case PAINTBRUSH: // ordinal 3
                case PENCIL: // ordinal 2
                    this.redraw();
                    this.freeDrawRasterize.drawNextStroke(event.getX(), event.getY());
                    break;

                default:
                    break;
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if(this.toolType == null)
                event.consume();

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.shapeRasterize.setLine(this.context.getStroke(), event.getX(), event.getY());
                    break;

                case RECTANGLE_DRAW: // ordinal 5
                case RECTANGLE_FILL: // ordinal 4
                    this.shapeRasterize.setRectangle(this.context.getStroke(), event.getX(), event.getY());
                    break;

                case ELLIPSE_DRAW: // ordinal 7
                case ELLIPSE_FILL: // ordinal 6
                    this.shapeRasterize.setEllipse(this.context.getStroke(), event.getX(), event.getY());
                    break;

                case PENCIL: // ordinal 2
                case PAINTBRUSH: // ordinal 3
                    this.freeDrawRasterize.resetStroke(this.context, this.context.getStroke(),
                            this.toolType.equals(ToolsEnum.PAINTBRUSH), this.lineWeight, event.getX(), event.getY());
                    break;

                default:
                    event.consume();
                    break;
            }
        });
        
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            if(this.toolType == null)
                event.consume();

            this.setCursor(this.toolType.getCursor());

            switch(this.toolType) {

                case LINE: // ordinal 0
                    this.addEdit(new LineOperator(this.shapeRasterize.getLine(), this.lineWeight));
                    break;

                case RECTANGLE_FILL: // ordinal 4
                    this.addEdit(new RectangleOperator(this.shapeRasterize.getRectangle(), null));
                    break;

                case RECTANGLE_DRAW: // ordinal 5
                    this.addEdit(new RectangleOperator(this.shapeRasterize.getRectangle(), this.lineWeight));
                    break;

                case ELLIPSE_FILL: // ordinal 6
                    this.addEdit(new EllipseOperator(this.shapeRasterize.getEllipse(), null));
                    break;

                case ELLIPSE_DRAW: // ordinal 7
                    this.addEdit(new EllipseOperator(this.shapeRasterize.getEllipse(), this.lineWeight));
                    break;

                case PAINTBRUSH: // ordinal 3
                case PENCIL: // ordinal 2
                    this.addEdit(new FreeDrawOperator(this.context.getStroke(), this.freeDrawRasterize.getDrawPoints(),
                            this.lineWeight, this.freeDrawRasterize.isBrush(), this.getWidth(), this.getHeight()));
                    break;

                default:
                    event.consume();
                    break;
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            if(this.toolType == null || this.toolType != ToolsEnum.BUCKET)
                event.consume();

            else {

                Paint fill = this.context.getStroke();

                double mouseX = event.getX();
                double mouseY = event.getY();

                double width = this.getWidth();
                double height = this.getHeight();

                this.addEdit(new BucketOperator(this.getSnapshot(), fill, this.fillTolerance, mouseX, mouseY, width, height));
            }
        });
    }

    /**
     *
     * @param edit
     */
    private void addEdit(PaintOperator edit) {

        this.editHistory.subList(this.editIndex + 1, this.editHistory.size()).clear();
        
        this.editHistory.add(edit);
        this.editIndex ++;
        
        this.imageController.setSaved(false);
        this.redraw();
    }

    /**
     *
     */
    private void redraw() {

        this.context.clearRect(0.0, 0.0, this.getWidth(), this.getHeight());
        if(this.isUndoDisabled()) return;

        double currWeight = this.context.getLineWidth();
        Paint currStroke = this.context.getStroke();
        Paint currFill = this.context.getFill();

        for(int i = 0; i <= this.editIndex; i ++)
            this.editHistory.get(i).draw(this.context);

        this.context.setLineWidth(currWeight);
        this.context.setStroke(currStroke);
        this.context.setFill(currFill);
    }

    /**
     *
     */
    public void undoEdit() {

        if(this.isUndoDisabled()) return;

        this.editIndex --;
        this.redraw();

        this.imageController.setSaved(false);
    }

    /**
     *
     */
    public void redoEdit() {

        if(this.isRedoDisabled()) return;

        this.editIndex ++;
        this.redraw();

        this.imageController.setSaved(false);
    }

    /**
     * @return Snapshot of the current canvas as an image
     */
    public WritableImage getSnapshot() {

        WritableImage writableImage = new WritableImage((int)this.getWidth(), (int)this.getHeight());
        this.snapshot(null, writableImage);

        return writableImage;
    }

    /**
     * Loads a new image for the background and clears edit history
     *
     * @param image The new image to be loaded
     */
    public void loadImage(@NotNull Image image) {

        this.editHistory.clear();
        this.editIndex = -1;

        this.addEdit(new ImageOperator(image, this.getWidth(), this.getHeight()));
    }

    public void setToolType(@Nullable ToolsEnum toolType) {

        this.toolType = toolType;
        if(this.toolType != null)
            this.setCursor(this.toolType.getCursor());
    }

    public void clearAll() {

        // Checks for previous operand to have been 'clear' so as to avoid spam
        if(editIndex >= 0 && this.editHistory.get(editIndex) instanceof ClearOperator)
            return;

        this.addEdit(new ClearOperator(Color.WHITE, this.getWidth(), this.getHeight()));
    }

    public void setLineWeight(double lineWeight) {
        this.lineWeight = lineWeight;
    }

    public void setFillTolerance(double fillTolerance) {
        this.fillTolerance = fillTolerance;
    }

    public void setColor(@NotNull Color color) {
        this.context.setStroke(color);
    }

    public void setImageController(@NotNull ImageController imageController) {
        this.imageController = imageController;
    }

    public boolean isUndoDisabled() {
        return this.editIndex < 0;
    }

    public boolean isRedoDisabled() {
        return this.editIndex >= this.editHistory.size() - 1;
    }
}
