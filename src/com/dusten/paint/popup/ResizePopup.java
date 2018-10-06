package com.dusten.paint.popup;

import com.dusten.paint.controllers.CanvasController;
import com.dusten.paint.controllers.ResizeController;
import com.dusten.paint.enums.FilesEnum;
import com.sun.istack.internal.NotNull;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Dusten Knull
 *
 * Custom popup window that prompts the user to resize the current canvas
 * to specifed values using two IntegerFields. Uses ResizePopup.fxml for
 * layout
 */
public class ResizePopup extends PopupWindow<ResizeController> {

    private static final String TITLE = "Resize Canvas";

    private CanvasController canvasController;

    public ResizePopup() throws IOException {

        super(TITLE, FilesEnum.RESIZEPOPUP_FXML);

        this.initStyle(StageStyle.UTILITY);
        this.controller.setParent(this);
    }

    /**
     * Shows the window when prompted to (the window starts out hidden and should
     * only show when the user chooses a specific menu option). The canvas size
     * values are intially gotten from the application's CanvasController instance
     * from the ImageHelper component, and then set back to that same instance
     * if they have been edited and the edits have been confirmed via the 'OK'
     * button
     *
     * @param canvasController The current running application's CanvasController instance
     */
    public void showAsPopup(@NotNull CanvasController canvasController) {

        if(this.canvasController == null)
            this.canvasController = canvasController;

        int currentWidth = this.canvasController.getCanvasWidth();
        int currentHeight = this.canvasController.getCanvasHeight();

        this.controller.setFieldValues(currentWidth, currentHeight);
        this.show();
    }

    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        this.canvasController.setCanvasSize(canvasWidth, canvasHeight);
    }
}
