package com.dusten.paint.components;

import com.dusten.paint.enums.FilesEnum;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

import java.io.InputStream;

/**
 * @author Dusten Knull
 *
 * Custom cursor using custom made images
 */
// TODO fix image corruption on Mac?
public class CustomCursor {

    public static final ImageCursor BUCKET = new CustomCursor(FilesEnum.BUCKET_CURSOR, 1, 1).getCursor();
    public static final ImageCursor PENCIL = new CustomCursor(FilesEnum.PENCIL_CURSOR, 0, 1).getCursor();
    public static final ImageCursor BRUSH = new CustomCursor(FilesEnum.PAINTBRUSH_CURSOR, 0, 1).getCursor();
    public static final ImageCursor MOVE = new CustomCursor(FilesEnum.MOVETOOL_CURSOR, 0, 0).getCursor();
    public static final ImageCursor ERASER = new CustomCursor(FilesEnum.ERASER_CURSOR, 0, 1).getCursor();
    public static final ImageCursor EYEDROPPER = new CustomCursor(FilesEnum.EYEDROPPER_CURSOR, 0, 1).getCursor();

    private InputStream fileStream;
    private double u;
    private double v;

    /**
     *
     * @param url Cursor image URL
     * @param u Coordinate for the x hotspot of the cursor relative to the image size (0.0-1.0)
     * @param v Coordinate for the y hotspot of the cursor relative to the image size (0.0-1.0)
     */
    private CustomCursor(FilesEnum url, double u, double v) {

        this.fileStream = CustomCursor.class.getResourceAsStream(url.toString());
        this.u = u;
        this.v = v;
    }

    /**
     *
     * @return
     */
    private ImageCursor getCursor() {

        Image image = new Image(this.fileStream);
        return new ImageCursor(image, image.getWidth() * this.u, image.getHeight() * this.v);
    }
}
