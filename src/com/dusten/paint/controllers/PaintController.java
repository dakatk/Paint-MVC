package com.dusten.paint.controllers;

import com.dusten.paint.helpers.CanvasHelper;
import com.dusten.paint.helpers.MenuHelper;
import com.dusten.paint.popup.ToolBarPopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * Controller for PaintApp.fxml
 */
public class PaintController implements Initializable {

    @FXML private MenuHelper menuHelper;
    @FXML private CanvasHelper canvasHelper;

    @FXML private ScrollBar verticalScroll;
    @FXML private ScrollBar horizontalScroll;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.canvasHelper.setLayoutY(this.menuHelper.getHeight());
        this.menuHelper.setImageHelper(this.canvasHelper);

        this.verticalScroll.setMin(0);
        this.horizontalScroll.setMin(0);

        this.verticalScroll.setOnScroll(event -> {
            // TODO adjust canvasHelper position
        });

        this.horizontalScroll.setOnScroll(event -> {
            // TODO adjust canvasHelper position
        });
    }

    public void checkScrollbarVisibility(double parentWidth, double parentHeight) {

        if(this.canvasHelper.getCanvas().getWidth() >= parentWidth) {

            this.horizontalScroll.setVisible(true);
            // TODO update horizontal scrollbar length
        }
        else this.horizontalScroll.setVisible(false);

        if(this.canvasHelper.getCanvas().getHeight() >= parentHeight - this.menuHelper.getHeight()) {

            this.verticalScroll.setVisible(true);
            // TODO update vertical scrollbar length
        }
        else this.verticalScroll.setVisible(false);
    }

    /**
     * @param mainStage The main Stage object to be used by both imageHelper and menuHelper
     */
    public void setMainStage(@NotNull Stage mainStage) {

        this.menuHelper.setMainStage(mainStage);
        this.canvasHelper.setMainStage(mainStage);
    }

    /**
     * @param toolBar The main ToolBarPopup object to be used with the program's menu options
     */
    public void setToolBar(@NotNull ToolBarPopup toolBar) {

        this.menuHelper.setToolBar(toolBar);
        toolBar.setCanvas(this.canvasHelper.getCanvas());
    }

    public CanvasHelper getCanvasHelper() {
        return this.canvasHelper;
    }
}
