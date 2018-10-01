package com.dusten.paint.popup;

import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PopupWindow<T> extends Stage {

    protected T controller;

    private double offsetX;
    private double offsetY;

    PopupWindow(String title, FilesEnum filesEnum) throws IOException
    {
        super();

        FXMLParser<T, Parent> fxmlParser = new FXMLParser<>(filesEnum);
        this.controller = fxmlParser.getController();

        this.offsetX = 0.0;
        this.offsetY = 0.0;

        this.setTitle(title);
        this.initStyle(StageStyle.UTILITY);
        this.setScene(new Scene(fxmlParser.getParent()));

        this.setResizable(false);
        this.toFront();
    }

    void setShowOffset(double offsetX, double offsetY) {

        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Shows the popup window and offsets it from the given Stage object
     *
     * @param stage The parent application window of this popup
     */
    public void showRelativeTo(Stage stage) {

        if(this.isShowing())
            this.requestFocus();

        if(stage != null) {

            this.setX(stage.getX() + this.offsetX);
            this.setY(stage.getY() + this.offsetY);
        }
        this.toBack();
        this.show();
    }
}
