package com.dusten.paint.popup;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ToolSettingsController;
import com.dusten.paint.enums.FilesEnum;
import com.sun.istack.internal.NotNull;

import java.io.IOException;

/**
 * @author Dusten Knull
 */
public class ToolSettingsPopup extends PopupWindow<ToolSettingsController> {

    public ToolSettingsPopup() throws IOException {
        super("Toolbar Settings", FilesEnum.TOOLSETTINGS_FXML);
    }

    public void selectTab(@NotNull String tabName) {
        this.controller.selectTab(tabName);
    }

    public void setToolBar(@NotNull ToolBarPopup toolBar) {
        this.controller.setToolBar(toolBar);
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.controller.setCanvas(canvas);
    }
}
