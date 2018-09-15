package com.dusten.paint.popup;

import com.dusten.paint.controllers.ImageController;
import com.dusten.paint.controllers.ResizeController;
import com.dusten.paint.enums.FilesEnum;
import com.sun.istack.internal.NotNull;

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

    private ImageController imageController;

    public ResizePopup() throws IOException {

        super(TITLE, FilesEnum.RESIZEPOPUP_FXML);
        this.controller.setParent(this);
    }

    /**
     * Shows the window when prompted to (the window starts out hidden and should
     * only show when the user chooses a specific menu option). The canvas size
     * values are intially gotten from the application's ImageController instance
     * from the ImageHelper component, and then set back to that same instance
     * if they have been edited and the edits have been confirmed via the 'OK'
     * button
     *
     * @param imageController The current running application's ImageController instance
     */
    public void showAsPopup(@NotNull ImageController imageController) {

        if(this.imageController == null)
            this.imageController = imageController;

        int currentWidth = this.imageController.getCanvasWidth();
        int currentHeight = this.imageController.getCanvasHeight();

        this.controller.setFieldValues(currentWidth, currentHeight);
        this.show();
    }

    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        this.imageController.setCanvasSize(canvasWidth, canvasHeight);
    }
}
