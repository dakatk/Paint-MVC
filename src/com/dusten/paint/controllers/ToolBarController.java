package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.components.ImageButton;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.popup.ToolBarPopup;
import com.dusten.paint.popup.ToolSettingsPopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * Controller for ToolBar.fxml
 */
public class ToolBarController implements Initializable {

    @FXML private ImageButton selectToolButton;
    @FXML private ImageButton paintBucketButton;
    @FXML private ImageButton eyeDropperButton;
    @FXML private ImageButton textToolButton;
    @FXML private ImageButton drawToolButton;
    @FXML private ImageButton lineToolButton;
    @FXML private ImageButton rectangleToolButton;
    @FXML private ImageButton ellipseToolButton;

    @FXML private ColorPicker primaryColorPicker;
    @FXML private ColorPicker secondaryColorPicker;

    private ToolSettingsPopup toolSettings;
    private ToolBarPopup parent;

    private DrawableCanvas canvas;
    private ImageButton nextToggle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            this.toolSettings = new ToolSettingsPopup();
            this.toolSettings.show();

        } catch(Exception e) {
            e.printStackTrace();
        }

        this.updatePrimaryColorAction();
        this.updateSecondaryColorAction();

        ToggleGroup toolButtonGroup = new ToggleGroup();
        toolButtonGroup.selectedToggleProperty().addListener((value, oldToggle, newToggle) ->
            this.nextToggle = (ImageButton)toolButtonGroup.getSelectedToggle()
        );

        List<ImageButton> buttonList = Arrays.asList(this.selectToolButton, this.paintBucketButton, this.eyeDropperButton,
                this.textToolButton, this.drawToolButton, this.lineToolButton, this.rectangleToolButton, this.ellipseToolButton);

        for(ImageButton button : buttonList) {

            button.setToggleGroup(toolButtonGroup);
            button.setOnAction(event -> {
                if(button.isSelected())
                    this.toolSettings.selectTab(button.getSettingsTab());
            });
        }

        /*
        this.selectToolButton.setOnAction(event -> {
            if(this.selectToolButton.isSelected())
                this.toolSettings.selectTab("Select");
        });

        this.paintBucketButton.setOnAction(event -> {
            if(this.paintBucketButton.isSelected())
                this.toolSettings.selectTab("Bucket");
        });

        this.selectToolButton.setToggleGroup(toolButtonGroup);
        this.paintBucketButton.setToggleGroup(toolButtonGroup);
        this.eyeDropperButton.setToggleGroup(toolButtonGroup);
        this.textToolButton.setToggleGroup(toolButtonGroup);
        this.drawToolButton.setToggleGroup(toolButtonGroup);
        this.lineToolButton.setToggleGroup(toolButtonGroup);
        this.rectangleToolButton.setToggleGroup(toolButtonGroup);
        this.ellipseToolButton.setToggleGroup(toolButtonGroup);*/
    }

    /**
     * Updates the primary color to be used by the canvas for drawing operations
     * and updates the tooltip for the primary ColorPicker component of the toolbar
     */
    @FXML
    private void updatePrimaryColorAction() {

        Color colorValue = this.primaryColorPicker.getValue();

        // Show color values as integers between 0 and 255 instead of
        // doubles from 0.0 - 1.0
        int redValue = (int)Math.ceil(colorValue.getRed() * 255);
        int greenValue = (int)Math.ceil(colorValue.getGreen() * 255);
        int blueValue = (int)Math.ceil(colorValue.getBlue() * 255);

        Tooltip tooltip = new Tooltip("Primary Color:\n R: " + redValue + ", G: " + greenValue + ", B: " + blueValue);
        this.primaryColorPicker.setTooltip(tooltip);

        if(this.canvas != null)
            this.canvas.setPrimaryColor(colorValue);
    }

    /**
     * Updates the secondary color to be used by the canvas for drawing operations
     * and updates the tooltip for the secondary ColorPicker component of the toolbar
     */
    @FXML
    private void updateSecondaryColorAction() {

        Color colorValue = this.secondaryColorPicker.getValue();

        // Show color values as integers between 0 and 255 instead of
        // doubles from 0.0 - 1.0
        int redValue = (int)Math.ceil(colorValue.getRed() * 255);
        int greenValue = (int)Math.ceil(colorValue.getGreen() * 255);
        int blueValue = (int)Math.ceil(colorValue.getBlue() * 255);

        Tooltip tooltip = new Tooltip("Secondary Color:\n R: " + redValue + ", G: " + greenValue + ", B: " + blueValue);
        this.secondaryColorPicker.setTooltip(tooltip);

        if(this.canvas != null)
            this.canvas.setSecondaryColor(colorValue);
    }

    /**
     * Shows the ToolBar settings popup windo
     */
    @FXML
    public void toggleSettingsAction() {

        if(this.toolSettings == null) return;

        if(this.toolSettings.isShowing())
            this.toolSettings.hide();
        else
            this.toolSettings.show();
    }

    /**
     * Swaps the primary and secondary color values
     */
    @FXML
    private void swapColorsAction() {

        Color primary = this.primaryColorPicker.getValue();
        Color secondary = this.secondaryColorPicker.getValue();

        this.primaryColorPicker.setValue(secondary);
        this.secondaryColorPicker.setValue(primary);

        this.updatePrimaryColorAction();
        this.updateSecondaryColorAction();
    }

    /**
     *
     * @param canvas
     */
    public void setCanvas(@NotNull DrawableCanvas canvas) {

        this.canvas = canvas;

        this.canvas.setPrimaryColor(this.primaryColorPicker.getValue());
        this.canvas.setSecondaryColor(this.secondaryColorPicker.getValue());

        if(this.toolSettings != null)
            this.toolSettings.setCanvas(this.canvas);

        this.canvas.setOnPrimaryColorChanged(color -> {

            this.primaryColorPicker.setValue(color);
            this.updatePrimaryColorAction();
        });

        this.canvas.setOnSecondaryColorChanged(color -> {

            this.secondaryColorPicker.setValue(color);
            this.updateSecondaryColorAction();
        });

        this.canvas.setOnMouseEntered(event -> {

            if(this.nextToggle == null) {

                this.canvas.setToolType(null);
                this.parent.getParentScene().setCursor(Cursor.DEFAULT);

            } else {

                this.canvas.setToolType(this.nextToggle.getEnumToolType());
                this.parent.getParentScene().setCursor(this.nextToggle.getEnumToolType().getCursor());
            }
        });

        this.canvas.setOnMouseExited(event ->
            this.parent.getParentScene().setCursor(Cursor.DEFAULT)
        );
    }

    /**
     * 
     * @param parent
     */
    public void setParent(@NotNull ToolBarPopup parent) {

        this.parent = parent;
        if(this.toolSettings != null)
            this.toolSettings.setToolBar(this.parent);

    }

    public void setSelectToolMode(@NotNull ToolsEnum toolType) {
        this.selectToolButton.setEnumToolType(toolType, this.canvas);
    }

    public void setRectangleToolMode(@NotNull ToolsEnum toolType) {
        this.rectangleToolButton.setEnumToolType(toolType, this.canvas);
    }

    public void setEllipseToolMode(@NotNull ToolsEnum toolType) {
        this.ellipseToolButton.setEnumToolType(toolType, this.canvas);
    }

    public void setDrawToolMode(@NotNull ToolsEnum toolType) {
        this.drawToolButton.setEnumToolType(toolType, this.canvas);
    }

    public void setLineToolMode(@NotNull ToolsEnum toolType) {
        this.lineToolButton.setEnumToolType(toolType, this.canvas);
    }

    public Stage getSettingsWindow() {
        return this.toolSettings;
    }
}
