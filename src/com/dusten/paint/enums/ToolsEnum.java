package com.dusten.paint.enums;

import com.dusten.paint.components.CustomCursor;
import javafx.scene.Cursor;

/**
 * @author Dusten Knull
 *
 * Descriptors for the higher level values for each tool
 * (currently the icon, tooltip, and mouse cursor). These objects
 * are also used to differentiate between which tool is
 * currently active (e.g., 'toolType.equals(ToolsEnum.LINE)')
 */
public enum ToolsEnum {

    LINE(FilesEnum.LINETOOL_ICON, Cursor.CROSSHAIR, "Line Tool"),
    BUCKET(FilesEnum.PAINTBUCKET_ICON, CustomCursor.BUCKET, "Paint Bucket Tool"),
    RECTANGLE_FILL(FilesEnum.RECTANGLEFILLTOOL_ICON, Cursor.CROSSHAIR, "Fill Rectangle Tool"),
    RECTANGLE_DRAW(FilesEnum.RECTANGLEDRAWTOOL_ICON, Cursor.CROSSHAIR, "Draw Rectangle Tool");

    private FilesEnum iconURL;
    private String tooltip;
    private Cursor cursor;

    ToolsEnum(FilesEnum iconURL, Cursor cursor, String tooltip) {

        this.iconURL = iconURL;
        this.tooltip = tooltip;
        this.cursor = cursor;
    }

    public String getIconURL() {
        return this.iconURL.toString();
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public Cursor getCursor() {
        return this.cursor;
    }
}
