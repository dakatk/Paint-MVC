package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;

public class ToolSettingsController implements Initializable {

    @FXML private Slider fillTolerance;
    @FXML private Slider lineWeight;

    private DrawableCanvas canvas;

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
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.canvas = canvas;
    }
}
