package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.helpers.CanvasHelper;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * {@link ControllerBase} for ImageHelper.fxml
 */
public class CanvasController extends ControllerBase {

    @FXML private Rectangle background;
    @FXML private DrawableCanvas canvas;

    private CanvasHelper parent;
    private boolean saved;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.canvas.setCanvasController(this);
        this.saved = true;
    }

    /**
     * Sets the size of the canvas in the window
     *
     * @param canvasWidth the new width of the canvas
     * @param canvasHeight the new height of the canvas
     */
    public void setCanvasSize(double canvasWidth, double canvasHeight) {

        this.parent.setMaxSize(canvasWidth, canvasHeight);
        this.parent.setMinSize(canvasWidth, canvasHeight);
        this.parent.setPrefSize(canvasWidth, canvasHeight);

        this.canvas.setWidth(canvasWidth);
        this.canvas.setHeight(canvasHeight);

        this.background.setWidth(canvasWidth);
        this.background.setHeight(canvasHeight);
    }

    /**
     * @param saved Application's save status for current loaded image
     */
    public void setSaved(boolean saved) {

        this.saved = saved;
        this.parent.updateSaveStatus(saved);
    }

    public void setImage(@NotNull Image image) {
        this.canvas.loadImage(image);
    }

    public void setParent(@NotNull CanvasHelper parent) {
        this.parent = parent;
    }

    public boolean isSaved() {
        return this.saved;
    }

    public int getCanvasWidth() {
        return (int)this.canvas.getWidth();
    }

    public int getCanvasHeight() {
        return (int)this.canvas.getHeight();
    }

    public WritableImage getImage() {
        return this.canvas.getSnapshot();
    }

    public DrawableCanvas getCanvas() {
        return this.canvas;
    }
}
