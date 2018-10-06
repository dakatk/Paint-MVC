package com.dusten.paint.popup;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ToolBarController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.enums.ToolsEnum;
import com.sun.istack.internal.NotNull;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Dusten Knull
 *
 * Toolbar window that is initially just to the left to the main window.
 * Can be moved around, hidden/unhidden, and contains buttons representing
 * each tool option for editing the canvas.
 */
public class ToolBarPopup extends PopupWindow<ToolBarController> {

    private Scene parentScene;

    public ToolBarPopup() throws IOException {

        super(null, FilesEnum.TOOLBAR_FXML);
        this.controller.setParent(this);
    }

    public Scene getParentScene() {
        return this.parentScene;
    }

    public Stage getSettingsWindow() {
        return this.controller.getSettingsWindow();
    }

    public void setParentScene(@NotNull Scene parentScene) {
        this.parentScene = parentScene;
    }

    public void toggleSettingsWindow() {
        this.controller.toggleSettingsAction();
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.controller.setCanvas(canvas);
    }

    public void setSelectToolMode(@NotNull ToolsEnum toolType) {
        this.controller.setSelectToolMode(toolType);
    }

    public void setRectangleToolMode(@NotNull ToolsEnum toolType) {
        this.controller.setRectangleToolMode(toolType);
    }

    public void setEllipseToolMode(@NotNull ToolsEnum toolType) {
        this.controller.setEllipseToolMode(toolType);
    }

    public void setDrawToolMode(@NotNull ToolsEnum toolType) {
        this.controller.setDrawToolMode(toolType);
    }

    public void setLineToolMode(@NotNull ToolsEnum toolType) {
        this.controller.setLineToolMode(toolType);
    }
}
