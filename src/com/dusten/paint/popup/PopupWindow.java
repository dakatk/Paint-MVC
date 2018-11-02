package com.dusten.paint.popup;

import com.dusten.paint.controllers.ControllerBase;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Window with extra UI that can be shown or hidden on command
 * and is always shown relative to another window
 *
 * @author Dusten Knull
 * @param <T> ControllerBase class of this popup window type
 */
public class PopupWindow<T extends ControllerBase> extends Stage {

    protected T controller;

    PopupWindow(String title, FilesEnum filesEnum) throws IOException {

        super();

        FXMLParser<T, Parent> fxmlParser = new FXMLParser<>(filesEnum);
        this.controller = fxmlParser.getController();

        this.setTitle(title);
        this.initStyle(StageStyle.UNDECORATED);
        this.setScene(new Scene(fxmlParser.getParent()));

        this.setResizable(false);
        this.toFront();
    }
}
