package com.dusten.paint.controllers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.components.ImageButton;
import com.dusten.paint.components.IntegerField;
import com.dusten.paint.enums.ToolsEnum;
import com.dusten.paint.popup.ToolBarPopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 */
public class ToolSettingsController implements Initializable {

    @FXML private ImageButton rectangleFillMode;
    @FXML private ImageButton rectangleDrawMode;
    @FXML private ImageButton ellipseFillMode;
    @FXML private ImageButton ellipseDrawMode;
    @FXML private ImageButton pencilDrawMode;
    @FXML private ImageButton brushDrawMode;
    @FXML private ImageButton eraserDrawMode;
    @FXML private ImageButton lineDrawMode;
    @FXML private ImageButton arcDrawMode;
    @FXML private ImageButton selectMode;
    @FXML private ImageButton moveMode;

    @FXML private ComboBox<String> fontNames;
    @FXML private Slider fillTolerance;
    @FXML private Slider lineWeight;

    @FXML private IntegerField fontSize;
    @FXML private Button fontSizeInc;
    @FXML private Button fontSizeDec;

    @FXML private TabPane parentPane;

    private HashMap<String, Tab> tabsByName;
    private DrawableCanvas canvas;
    private ToolBarPopup toolBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.fontNames.itemsProperty().get().addAll(Font.getFamilies());
        this.fontNames.valueProperty().addListener((observable, oldValue, newValue) -> {

            this.fontNames.setTooltip(new Tooltip(newValue));

            if(this.toolBar != null)
                this.toolBar.selectTool(ToolsEnum.TEXT);

            if(this.canvas != null)
                this.canvas.setFontName(newValue);
        });

        this.fontNames.getSelectionModel().select(0);

        this.fillTolerance.valueProperty().addListener((observable, oldValue, newValue) -> {

            this.fillTolerance.setTooltip(new Tooltip(String.format("%.2f%%", this.fillTolerance.getValue())));

            if(this.toolBar != null)
                this.toolBar.selectTool(ToolsEnum.BUCKET);

            if(this.canvas != null)
                this.canvas.setFillTolerance(this.fillTolerance.getValue() / 100.0);
        });

        this.lineWeight.valueProperty().addListener((observable, oldValue, newValue) -> {

            this.lineWeight.setTooltip(new Tooltip(String.valueOf((int)this.lineWeight.getValue()) + "px"));

            if(this.canvas != null)
                this.canvas.setLineWeight(this.lineWeight.getValue());
        });

        this.fontSize.setOnUpdateCall(() -> {

            this.fontSizeInc.setDisable(this.fontSize.atMaxValue());
            this.fontSizeDec.setDisable(this.fontSize.atMinValue());

            if(this.canvas != null)
                this.canvas.setFontWeight(this.fontSize.getValue());

            return null;
        });

        this.fillTolerance.adjustValue(DrawableCanvas.DEFAULT_FILL_TOLERANCE * 100);
        this.lineWeight.adjustValue(DrawableCanvas.DEFAULT_LINE_WEIGHT);
        this.fontSize.setValue(12);

        ToggleGroup selectModes = new ToggleGroup();
        selectModes.selectedToggleProperty().addListener((observable, oldValue, newVale) -> {

            ImageButton selectedMode = (ImageButton)selectModes.getSelectedToggle();
            if(selectedMode == null) return;

            if(this.toolBar != null)
                this.toolBar.setSelectToolMode(selectedMode.getEnumToolType());
        });

        this.selectMode.setToggleGroup(selectModes);
        this.moveMode.setToggleGroup(selectModes);

        ToggleGroup rectangleModes = new ToggleGroup();
        rectangleModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)rectangleModes.getSelectedToggle();
            if(selectedMode == null) return;

            if(this.toolBar != null)
                this.toolBar.setRectangleToolMode(selectedMode.getEnumToolType());
        });

        this.rectangleFillMode.setToggleGroup(rectangleModes);
        this.rectangleDrawMode.setToggleGroup(rectangleModes);

        ToggleGroup ellipseModes = new ToggleGroup();
        ellipseModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)ellipseModes.getSelectedToggle();
            if(selectedMode == null) return;

            if(this.toolBar != null)
                this.toolBar.setEllipseToolMode(selectedMode.getEnumToolType());
        });

        this.ellipseFillMode.setToggleGroup(ellipseModes);
        this.ellipseDrawMode.setToggleGroup(ellipseModes);

        ToggleGroup drawModes = new ToggleGroup();
        drawModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)drawModes.getSelectedToggle();
            if(selectedMode == null) return;

            if(this.toolBar != null)
                this.toolBar.setDrawToolMode(selectedMode.getEnumToolType());
        });

        this.eraserDrawMode.setToggleGroup(drawModes);
        this.pencilDrawMode.setToggleGroup(drawModes);
        this.brushDrawMode.setToggleGroup(drawModes);

        ToggleGroup lineModes = new ToggleGroup();
        lineModes.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            ImageButton selectedMode = (ImageButton)lineModes.getSelectedToggle();
            if(selectedMode == null) return;

            if(this.toolBar != null)
                this.toolBar.setLineToolMode(selectedMode.getEnumToolType());
        });

        this.lineDrawMode.setToggleGroup(lineModes);
        this.arcDrawMode.setToggleGroup(lineModes);

        this.tabsByName = new HashMap<>();
        for(Tab tab : this.parentPane.getTabs())
            this.tabsByName.put(tab.getText(), tab);
    }

    @FXML
    private void incrementFontSize() {

        this.fontSize.setValue(this.fontSize.getValue() + 1);

        this.fontSizeInc.setDisable(this.fontSize.atMaxValue());
        this.fontSizeDec.setDisable(false);
    }

    @FXML
    private void decrementFontSize() {

        this.fontSize.setValue(this.fontSize.getValue() - 1);

        this.fontSizeDec.setDisable(this.fontSize.atMinValue());
        this.fontSizeDec.setDisable(false);
    }

    public void selectTab(@NotNull String tabName) {
        this.parentPane.getSelectionModel().select(this.tabsByName.get(tabName));
    }

    public void setToolBar(@NotNull ToolBarPopup toolBar) {
        this.toolBar = toolBar;
    }

    public void setCanvas(@NotNull DrawableCanvas canvas) {
        this.canvas = canvas;
    }
}
