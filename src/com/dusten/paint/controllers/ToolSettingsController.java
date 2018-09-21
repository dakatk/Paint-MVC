package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.components.ImageButton;
import com.dusten.paint.main.ToolBar;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class ToolSettingsController implements Initializable {

    @FXML private ImageButton rectangleFillMode;
    @FXML private ImageButton rectangleDrawMode;
    @FXML private ImageButton ellipseFillMode;
    @FXML private ImageButton ellipseDrawMode;
    @FXML private ImageButton pencilDrawMode;
    @FXML private ImageButton brushDrawMode;
    @FXML private Slider fillTolerance;
    @FXML private Slider lineWeight;

    private DrawableCanvas canvas;
    private ToolBar toolBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.fillTolerance.adjustValue(DrawableCanvas.DEFAULT_FILL_TOLERANCE * 100);
        this.lineWeight.adjustValue(DrawableCanvas.DEFAULT_LINE_WEIGHT);

        this.fillTolerance.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(this.canvas == null) return;
            this.canvas.setFillTolerance(this.fillTolerance.getValue() / 100.0);
        });

        this.lineWeight.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(this.canvas == null) return;
            this.canvas.setLineWeight(this.lineWeight.getValue());
        });

        ToggleGroup rectangleModes = new ToggleGroup();
        rectangleModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)rectangleModes.getSelectedToggle();

            if(this.toolBar != null)
                this.toolBar.setRectangleToolMode(selectedMode.getEnumToolType());
        });

        this.rectangleFillMode.setToggleGroup(rectangleModes);
        this.rectangleDrawMode.setToggleGroup(rectangleModes);

        ToggleGroup ellipseModes = new ToggleGroup();
        ellipseModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)ellipseModes.getSelectedToggle();

            if(this.toolBar != null)
                this.toolBar.setEllipseToolMode(selectedMode.getEnumToolType());
        });

        this.ellipseFillMode.setToggleGroup(ellipseModes);
        this.ellipseDrawMode.setToggleGroup(ellipseModes);

        ToggleGroup drawModes = new ToggleGroup();
        drawModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)drawModes.getSelectedToggle();

            if(this.toolBar != null)
                this.toolBar.setDrawToolMode(selectedMode.getEnumToolType());
        });

        this.pencilDrawMode.setToggleGroup(drawModes);
        this.brushDrawMode.setToggleGroup(drawModes);
    }

    public void setToolBar(@NotNull ToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.canvas = canvas;
    }
}
