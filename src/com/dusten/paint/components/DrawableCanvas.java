package com.dusten.paint.components;

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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dusten Knull
 */
public class DrawableCanvas extends Canvas {

    public static final double DEFAULT_FILL_TOLERANCE = 0.1;
    public static final double DEFAULT_LINE_WEIGHT = 2.0;

    private ImageController imageController;
    private ShapeRasterize shapeRasterize;
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

            else if(this.toolType.equals(ToolsEnum.LINE)) {

                this.redraw();
                this.shapeRasterize.renderLine(this.context, this.lineWeight, event.getX(), event.getY());
            }

            else if(this.toolType.equals(ToolsEnum.RECTANGLE_FILL)) {

                this.redraw();
                this.shapeRasterize.renderFillRectangle(this.context, event.getX(), event.getY());
            }

            else if(this.toolType.equals(ToolsEnum.RECTANGLE_DRAW)) {

                this.redraw();
                this.shapeRasterize.renderDrawRectangle(this.context, this.lineWeight, event.getX(), event.getY());
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if(this.toolType == null)
                event.consume();

            else if(this.toolType.equals(ToolsEnum.LINE))
                this.shapeRasterize.setLine(this.context.getStroke(), event.getX(), event.getY());

            else if(this.toolType.equals(ToolsEnum.RECTANGLE_FILL) || this.toolType.equals(ToolsEnum.RECTANGLE_DRAW))
                this.shapeRasterize.setRectangle(this.context.getStroke(), event.getX(), event.getY());

        });
        
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            this.setCursor(this.toolType.getCursor());
           
            if(this.toolType == null)
                event.consume();

            else if(this.toolType.equals(ToolsEnum.LINE)) {

                Line line = this.shapeRasterize.getLine();
                this.addEdit(new LineOperator(line, this.lineWeight));
            }
            else if(this.toolType.equals(ToolsEnum.RECTANGLE_FILL) || this.toolType.equals(ToolsEnum.RECTANGLE_DRAW)) {

                Rectangle rectangle = this.shapeRasterize.getRectangle();
                this.addEdit(new RectangleOperator(rectangle, this.lineWeight, this.toolType.equals(ToolsEnum.RECTANGLE_DRAW)));
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{

            if(this.toolType == null)
                event.consume();

            else if(this.toolType.equals(ToolsEnum.BUCKET)) {

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
        this.setCursor(this.toolType.getCursor());
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
