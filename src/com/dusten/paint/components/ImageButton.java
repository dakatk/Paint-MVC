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
public class ImageButton extends ToggleButton {

    private ToolsEnum enumToolType;

    public ImageButton(@NamedArg("toolType") String toolType) {

        this.enumToolType = ToolsEnum.valueOf(toolType);

        Image baseImage = new Image(getClass().getResourceAsStream(this.enumToolType.getIconURL()));
        ImageView graphic = new ImageView(baseImage);

        graphic.setPreserveRatio(true);

        graphic.setFitWidth(30.0);
        graphic.setFitHeight(40.0);

        this.setGraphic(graphic);
        this.setTooltip(new Tooltip(this.enumToolType.getTooltip()));
    }

    /**
     * @return The ToolsEnum object for the current tool selected
     */
    public ToolsEnum getEnumToolType() {
        return this.enumToolType;
    }
}
