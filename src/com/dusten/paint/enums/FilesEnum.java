package com.dusten.paint.enums;

/**
 * @author Dusten Knull
 *
 * All of the application's important files expressed as constants with
 * the file URL expressed as a string member to the class
 */
public enum FilesEnum {

    // fxml files
    MAIN_FXML("PaintApp.fxml"),
    MENUHELPER_FXML("MenuHelper.fxml"),
    IMAGEHELPER_FXML("ImageHelper.fxml"),
    RESIZEPOPUP_FXML("ResizePopup.fxml"),
    TOOLBAR_FXML("ToolBar.fxml"),
    TOOLSETTINGS_FXML("ToolSettings.fxml"),
    // toolbar icon image files
    PAINTBUCKET_ICON("resources/PaintBucket.png"),
    LINETOOL_ICON("resources/LineTool.png"),
    RECTANGLEFILLTOOL_ICON("resources/RectangleFillTool.png"),
    RECTANGLEDRAWTOOL_ICON("resources/RectangleDrawTool.png"),
    // custom cursor image files
    BUCKET_CURSOR("resources/cursors/PaintBucketCursor.png");

    private String url;

    FilesEnum(String url) {
        this.url = url;
    }

    public String toString() {
        return this.url;
    }
}
