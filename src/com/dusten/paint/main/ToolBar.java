package com.dusten.paint.main;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ToolBarController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.popup.PopupWindow;
import com.sun.istack.internal.NotNull;

import java.io.IOException;

/**
 * @author Dusten Knull
 *
 * Toolbar window that is initially just to the left to the main window.
 * Can be moved around, hidden/unhidden, and contains buttons representing
 * each tool option for editing the canvas.
 */
public class ToolBar extends PopupWindow<ToolBarController> {

    ToolBar() throws IOException {

        super(null, FilesEnum.TOOLBAR_FXML);

        this.controller.setParent(this);

        this.setShowOffset(-70.0, 0.0);
        this.setOnCloseRequest(event -> this.controller.closeSettingsWindow());
    }

    public void setRectangleToolMode(ToolsEnum toolType) {
        this.controller.setRectangleToolMode(toolType);
    }

    public void showSettingsWindow() {
        this.controller.showSettingsAction();
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.controller.setCanvas(canvas);
    }
}
