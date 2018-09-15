package com.dusten.paint.main;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ToolsController;
import com.dusten.paint.enums.FilesEnum;
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
public class ToolBar extends PopupWindow<ToolsController> {

    ToolBar() throws IOException {

        super(null, FilesEnum.TOOLBAR_FXML);
        this.setShowOffset(70.0, 0.0);
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.controller.setCanvas(canvas);
    }
}
