package com.dusten.paint.components;

import com.dusten.paint.components.primitives.Rectangle;
import com.dusten.paint.operators.MoveOperator;
import javafx.scene.image.Image;

import java.awt.*;

class Clipboard {

    private Rectangle sourceRect;
    private Image sourceImage;

    private boolean cut;

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

    MoveOperator getAsMoveOperator() {
        return new MoveOperator(this.sourceImage, this.sourceRect, new Point(0, 0), this.cut);
    }
}
