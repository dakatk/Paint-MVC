package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.components.ImageButton;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.main.ToolBar;
import com.dusten.paint.popup.ToolSettingsPopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * Controller for ToolBar.fxml
 */
public class ToolBarController implements Initializable {

    @FXML private ImageButton paintBucketButton;
    @FXML private ImageButton lineToolButton;
    @FXML private ImageButton rectangleToolButton;
    @FXML private ImageButton ellipseToolButton;
    @FXML private ColorPicker colorPicker;

    private ToolSettingsPopup toolSettings;
    private ToolBar parent;

    private DrawableCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            this.toolSettings = new ToolSettingsPopup();
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.updateColorAction();

        ToggleGroup buttonGroup = new ToggleGroup();
        buttonGroup.selectedToggleProperty().addListener((value, oldToggle, newToggle) -> {

            if(this.canvas == null)
                return;

            ImageButton nextToggle = (ImageButton)buttonGroup.getSelectedToggle();

            if(nextToggle == null)
                this.canvas.setToolType(null);
            else
                this.canvas.setToolType(nextToggle.getEnumToolType());
        });

        this.paintBucketButton.setToggleGroup(buttonGroup);
        this.lineToolButton.setToggleGroup(buttonGroup);
        this.rectangleToolButton.setToggleGroup(buttonGroup);
        this.ellipseToolButton.setToggleGroup(buttonGroup);
    }

    /**
     * Updates the color to be used by the canvas for drawing operations
     * and updates the tooltip for the ColorPicker component of the toolbar
     */
    @FXML
    private void updateColorAction() {

        Color colorValue = this.colorPicker.getValue();

        // Show color values as integers between 0 and 255 instead of
        // doubles from 0.0 - 1.0
        int redValue = (int)Math.ceil(colorValue.getRed() * 255);
        int greenValue = (int)Math.ceil(colorValue.getGreen() * 255);
        int blueValue = (int)Math.ceil(colorValue.getBlue() * 255);

        this.colorPicker.setTooltip(new Tooltip("Color Chooser\nCurrent: R: " + redValue + ", G: " + greenValue + ", B: " + blueValue));

        if(this.canvas != null)
            this.canvas.setColor(colorValue);
    }

    /**
     * Shows the ToolBar settings popup windo
     */
    @FXML
    public void showSettingsAction() {

        if(this.toolSettings == null) return;
        this.toolSettings.showRelativeTo(this.parent);
    }
    public void closeSettingsWindow() {

        if(this.toolSettings == null) return;
        this.toolSettings.close();
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {

        this.canvas = canvas;
        this.canvas.setColor(this.colorPicker.getValue());

        if(this.toolSettings != null)
            this.toolSettings.setCanvas(this.canvas);
    }

    public void setParent(@NotNull ToolBar parent) {

        this.parent = parent;
        if(this.toolSettings != null)
            this.toolSettings.setToolBar(this.parent);

    }

    public void setRectangleToolMode(@NotNull ToolsEnum toolType) {

        this.rectangleToolButton.setEnumToolType(toolType);
        if(this.rectangleToolButton.isSelected())
            this.canvas.setToolType(toolType);
    }

    public void setEllipseToolMode(@NotNull ToolsEnum toolType) {

        this.ellipseToolButton.setEnumToolType(toolType);
        if(this.ellipseToolButton.isSelected())
            this.canvas.setToolType(toolType);
    }
}
