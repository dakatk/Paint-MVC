package com.dusten.paint.components;

import com.dusten.paint.primitives.Rectangle;
import com.dusten.paint.operators.MoveOperator;
import javafx.scene.image.Image;

import java.awt.*;

/**
 * @author Dusten Knull
 */
class Clipboard {

    private Rectangle sourceRect;
    private Image sourceImage;

    private boolean cut;

    /**
     *
     * @param sourceImage The source image
     * @param sourceRect The source area of the image to draw
     * @param cut Whether or not to cut versus copy
     */
    Clipboard(Image sourceImage, Rectangle sourceRect, boolean cut) {

        this.sourceImage = sourceImage;
        this.sourceRect = sourceRect;
        this.cut = cut;
    }

    double getSelectedWidth() {
        return this.sourceRect.getWidth();
    }

    double getSelectedHeight() {
        return this.sourceRect.getHeight();
    }

    /**
     * @return The clipboard data as a MoveOperator object for use in Edit History
     */
    MoveOperator getAsMoveOperator() {
        return new MoveOperator(this.sourceImage, this.sourceRect, new Point(0, 0), this.cut);
    }
}
