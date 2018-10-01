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
    PENCIL(FilesEnum.PENCILTOOL_ICON, CustomCursor.PENCIL, "Pencil Tool"),
    PAINTBRUSH(FilesEnum.PAINTBRUSHTOOL_ICON, CustomCursor.BRUSH, "Paintbrush Tool"),
    RECTANGLE_FILL(FilesEnum.RECTANGLEFILLTOOL_ICON, Cursor.CROSSHAIR, "Fill Rectangle Tool"),
    RECTANGLE_DRAW(FilesEnum.RECTANGLEDRAWTOOL_ICON, Cursor.CROSSHAIR, "Draw Rectangle Tool"),
    ELLIPSE_FILL(FilesEnum.ELLIPSEFILLTOOL_ICON, Cursor.CROSSHAIR, "Fill Ellipse Tool"),
    ELLIPSE_DRAW(FilesEnum.ELLIPSEDRAWTOOL_ICON, Cursor.CROSSHAIR, "Draw Ellipse Tool"),
    SELECT_AREA(FilesEnum.SELECTTOOL_ICON, Cursor.CROSSHAIR, "Selection Tool"),
    MOVE_AREA(FilesEnum.MOVETOOL_ICON, Cursor.CROSSHAIR, "Move Selection Tool");

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
