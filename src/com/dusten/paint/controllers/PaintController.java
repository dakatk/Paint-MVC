package com.dusten.paint.controllers;

import com.dusten.paint.helpers.ImageHelper;
import com.dusten.paint.helpers.MenuHelper;
import com.dusten.paint.main.ToolBar;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML private ImageHelper imageHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.imageHelper.setLayoutY(this.menuHelper.getHeight());
        this.menuHelper.setImageHelper(this.imageHelper);
    }

    /**
     * @param mainStage The main Stage object to be used by both imageHelper and menuHelper
     */
    public void setMainStage(@NotNull Stage mainStage) {

        this.menuHelper.setMainStage(mainStage);
        this.imageHelper.setMainStage(mainStage);
    }

    public void setToolBar(@NotNull ToolBar toolBar) {

        this.menuHelper.setToolBar(toolBar);
        toolBar.setCanvas(this.imageHelper.getCanvas());
    }

    public ImageHelper getImageHelper() {
        return this.imageHelper;
    }
}
