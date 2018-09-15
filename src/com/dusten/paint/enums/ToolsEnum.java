package com.dusten.paint.enums;

import javafx.scene.Cursor;

/**
 * @author Dusten Knull
 *
 * Descriptors for the higher level values for each tool
 * (currently the icon and tooltip). These objects are
 * also used to differentiate between which tool is
 * currently active (e.g., 'toolType.equals(ToolsEnum.LINE)')
 */
public enum ToolsEnum {

    LINE(FilesEnum.LINETOOL_ICON, Cursor.CROSSHAIR, "Line Tool"),
    // TODO image cursor
    BUCKET(FilesEnum.PAINTBUCKET_ICON, Cursor.DEFAULT, "Paint Bucket Tool");

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
