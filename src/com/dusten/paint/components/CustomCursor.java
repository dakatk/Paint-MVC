package com.dusten.paint.components;

import com.dusten.paint.enums.FilesEnum;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

// TODO image corruption
public class CustomCursor {

    public static final ImageCursor BUCKET = new CustomCursor(FilesEnum.BUCKET_CURSOR).imageCursor;

    private ImageCursor imageCursor;

    private CustomCursor(FilesEnum url) {

        Image image = new Image(CustomCursor.class.getResourceAsStream(url.toString()));
        this.imageCursor = new ImageCursor(image, image.getWidth(), image.getHeight());
    }
}
