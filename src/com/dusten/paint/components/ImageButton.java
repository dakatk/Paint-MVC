package com.dusten.paint.components;

import com.dusten.paint.enums.ToolsEnum;
import javafx.beans.NamedArg;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Dusten Knull
 *
 * A togglable button with an image representaion instead of text.
 * Used primarily for the ToolBar window
 */
@SuppressWarnings("unused")
public class ImageButton extends ToggleButton {

    private ToolsEnum enumToolType;
    private String settingsTab;

    public ImageButton(@NamedArg("toolType") String toolType) {
        this(toolType, null);
    }

    public ImageButton(@NamedArg("toolType") String toolType, @NamedArg("settingsTab") String settingsTab) {

        this.setEnumToolType(ToolsEnum.valueOf(toolType), null);
        this.settingsTab = settingsTab;
    }

    /**
     *
     * @param url
     * @return
     */
    private ImageView createGraphic(String url) {

        Image baseImage = new Image(getClass().getResourceAsStream(url));
        ImageView graphic = new ImageView(baseImage);

        graphic.setPreserveRatio(true);

        graphic.setFitWidth(30.0);
        graphic.setFitHeight(40.0);

        return graphic;
    }

    /**
     *
     * @param enumToolType
     * @param canvas
     */
    public void setEnumToolType(ToolsEnum enumToolType, DrawableCanvas canvas) {

        this.enumToolType = enumToolType;

        if(this.isSelected() && canvas != null)
            canvas.setToolType(this.enumToolType);

        this.setGraphic(this.createGraphic(this.enumToolType.getIconURL()));
        this.setTooltip(new Tooltip(this.enumToolType.getTooltip()));
    }

    /**
     * @return The ToolsEnum object for the current tool selected
     */
    public ToolsEnum getEnumToolType() {
        return this.enumToolType;
    }

    public String getSettingsTab() {
        return this.settingsTab;
    }
}
