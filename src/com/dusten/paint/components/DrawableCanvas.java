package com.dusten.paint.components;

import com.dusten.paint.controllers.ImageController;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.operators.BucketOperator;
import com.dusten.paint.operators.ImageOperator;
import com.dusten.paint.operators.LineOperator;
import com.dusten.paint.operators.PaintOperator;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dusten Knull
 */
public class DrawableCanvas extends Canvas {

    // weight of the lines to be drawn
    // TODO make variable from edit menu
    private static final double LINE_STROKE = 2.0;

    private ImageController imageController;
    private GraphicsContext context;
    private ToolsEnum toolType;

    private List<PaintOperator> editHistory;
    private Line tempLine;
    private int editIndex;

    public DrawableCanvas() {

        this.editHistory = new ArrayList<>();
        this.editIndex = -1;

        this.tempLine = new Line();
        this.toolType = null;

        this.context = this.getGraphicsContext2D();
        this.context.setFill(Color.WHITE);
        this.context.setStroke(Color.WHITE);
        this.context.setLineWidth(LINE_STROKE);

        this.createAndSetEvents();
    }

    /**
     * Initial setup for all mouse events handled by the canvas
     */
    private void createAndSetEvents() {

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {

            if(this.toolType == null || !this.toolType.equals(ToolsEnum.LINE))
                event.consume();
            else
                this.moveLine(event.getX(), event.getY());
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {

            if(this.toolType == null || !this.toolType.equals(ToolsEnum.LINE))
                event.consume();
            else
                this.setLine(event.getX(), event.getY());
        });
        
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
           
            if(this.toolType == null || !this.toolType.equals(ToolsEnum.LINE))
                event.consume();
            else
                this.addEdit(new LineOperator(this.tempLine));
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{

            if(this.toolType == null || !this.toolType.equals(ToolsEnum.BUCKET))
                event.consume();

            else {

                Paint fill = this.context.getStroke();

                double mouseX = event.getX();
                double mouseY = event.getY();

                double width = this.getWidth();
                double height = this.getHeight();

                this.addEdit(new BucketOperator(this.getSnapshot(), fill, mouseX, mouseY, width, height));
            }
        });
    }

    /**
     * Move current line on screen to a new endX and endY based on the
     * mouse position, then redraw
     *
     * @param mouseX Current mouse pointer 'x' position on canvas
     * @param mouseY Current mouse pointer 'y' position on canvas
     */
    private void moveLine(double mouseX, double mouseY) {

        this.tempLine.setEndX(mouseX);
        this.tempLine.setEndY(mouseY);

        this.redraw();

        Paint currStroke = this.context.getStroke();

        this.context.setStroke(this.tempLine.getStroke());
        this.context.strokeLine(this.tempLine.getStartX(), this.tempLine.getStartY(),
                this.tempLine.getEndX(), this.tempLine.getEndY());

        this.context.setStroke(currStroke);
    }

    /**
     * Sets the current line's initial position to be at the
     * current mouse position
     *
     * @param mouseX Current mouse pointer 'x' position on canvas
     * @param mouseY Current mouse pointer 'y' position on canvas
     */
    private void setLine(double mouseX, double mouseY) {

        this.tempLine.setStartX(mouseX);
        this.tempLine.setStartY(mouseY);

        this.tempLine.setEndX(mouseX);
        this.tempLine.setEndY(mouseY);

        this.tempLine.setStroke(this.context.getStroke());
    }
    
    private void addEdit(PaintOperator edit) {

        this.editHistory.subList(this.editIndex + 1, this.editHistory.size()).clear();
        
        this.editHistory.add(edit);
        this.editIndex ++;
        
        this.imageController.setSaved(false);
        this.redraw();
    }

    private void redraw() {

        this.context.clearRect(0.0, 0.0, this.getWidth(), this.getHeight());
        if(this.isUndoDisabled()) return;

        Paint currStroke = this.context.getStroke();

        for(int i = 0; i <= this.editIndex; i ++)
            this.editHistory.get(i).draw(this.context);

        this.context.setStroke(currStroke);
    }

    public void undoEdit() {

        if(this.isUndoDisabled()) return;

        this.editIndex --;
        this.redraw();

        this.imageController.setSaved(false);
    }

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
    }

    public void setColor(@NotNull Color color) {
        this.context.setStroke(color);
    }

    public void setImageController(@NotNull ImageController imageController) { this.imageController = imageController; }

    public boolean isUndoDisabled() {
        return this.editIndex < 0;
    }

    public boolean isRedoDisabled() {
        return this.editIndex >= this.editHistory.size() - 1;
    }
}
