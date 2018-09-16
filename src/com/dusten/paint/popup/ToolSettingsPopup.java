package com.dusten.paint.popup;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ToolSettingsController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.main.ToolBar;
import com.sun.istack.internal.NotNull;

import java.io.IOException;

public class ToolSettingsPopup extends PopupWindow<ToolSettingsController> {

    public ToolSettingsPopup() throws IOException {

        super("Toolbar Settings", FilesEnum.TOOLSETTINGS_FXML);
        this.setShowOffset(80.0, 10.0);
    }

    public void setToolBar(@NotNull ToolBar toolBar) {
        this.controller.setToolBar(toolBar);
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.controller.setCanvas(canvas);
    }
}
