package com.dusten.paint.fxml;

import com.dusten.paint.enums.FilesEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Used for universally loading values from FXML files
 *
 * @author Dusten Knull
 * @param <T> Controller class type
 * @param <U> Parent class type
 */
public class FXMLParser<T, U extends Node> {

    private T controller;
    private U parent;

    public FXMLParser(FilesEnum url) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.valueOf(url)));

        // The top level component of the FXML file:
        this.parent = fxmlLoader.load();
        // The controller specified in the top level component:
        this.controller = fxmlLoader.getController();
    }

    /**
     * @return The controller instance specified in the loaded FXML file
     */
    public T getController() {
        return this.controller;
    }

    /**
     * @return The object created from loading the FXML file's root component
     */
    public U getParent() {
        return this.parent;
    }
}
