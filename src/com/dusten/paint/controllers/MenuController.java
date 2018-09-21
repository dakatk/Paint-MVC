package com.dusten.paint.controllers;

import com.dusten.paint.helpers.ImageHelper;
import com.dusten.paint.main.ToolBar;
import com.dusten.paint.popup.ResizePopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * Controller for MenuHelper.fxml
 */
public class MenuController implements Initializable {

    @FXML private MenuItem saveMenu;
    @FXML private MenuItem saveAsMenu;
    @FXML private MenuItem showToolsMenu;
    @FXML private MenuItem hideToolsMenu;
    @FXML private MenuItem undoMenu;
    @FXML private MenuItem redoMenu;

    private ResizePopup resizePopup;
    private ImageHelper imageHelper;
    private ToolBar toolBar;
    private Stage mainStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{
            this.resizePopup = new ResizePopup();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to load an image file via the specified
     * ImageComponent's 'loadImage' function
     */
    @FXML
    private void loadAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.loadImage();
    }

    /**
     * Prompts the user to save the current image file via the specified
     * ImageComponent's 'saveImage' function
     */
    @FXML
    private void saveAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.saveImage();
    }

    /**
     * Prompts the user to save the current image file as a new file
     * via the specified ImageComponent's 'saveImageAs' function
     */
    @FXML
    private void saveAsAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.saveImageAs();
    }

    /**
     * Prompts the user to close the application. If the user has not saved
     * the most recent changes made to the image, the user is then prompted
     * for one last chance to save before quitting, with the option to cancel
     * in the case of a misclick
     */
    @FXML
    private void closeAction() {

        if(this.mainStage == null) return;
        this.mainStage.getOnCloseRequest().handle(new WindowEvent(this.mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Shows a custom prompt window for the user to resize the width and height
     * of the current canvas via a window with two IntegerFields
     */
    @FXML
    private void resizeAction() {

        if(this.resizePopup == null) return;
        this.resizePopup.showAsPopup(this.imageHelper.getController());
    }

    /**
     *
     */
    @FXML
    private void clearCanvasAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.clearCanvas();
    }

    /**
     *
     */
    @FXML
    private void undoAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.undoEdit();

        if(this.redoMenu.isDisable())
            this.redoMenu.setDisable(false);
    }

    /**
     *
     */
    @FXML
    private void redoAction() {

        if(this.imageHelper == null) return;
        this.imageHelper.redoEdit();

        if(this.undoMenu.isDisable())
            this.undoMenu.setDisable(false);
    }

    /**
     *
     */
    @FXML
    private void showToolbarAction() {

        if(this.toolBar == null) return;
        this.toolBar.showRelativeTo(this.mainStage);

        this.hideToolsMenu.setDisable(false);
        this.showToolsMenu.setDisable(true);
    }

    /**
     *
     */
    @FXML
    private void hideToolbarAction() {

        if(this.toolBar == null) return;
        this.toolBar.hide();

        this.showToolsMenu.setDisable(false);
        this.hideToolsMenu.setDisable(true);
    }

    /**
     *
     */
    @FXML
    private void showToolSettingsAction() {

        if(this.toolBar == null) return;
        this.toolBar.showSettingsWindow();
    }

    /**
     * Sets the application's toolbar behavior in relation to
     * the application's menu entries under the 'Toolbar' menu
     *
     * @param toolBar The application's current toolbar
     */
    public void setToolBar(@NotNull ToolBar toolBar) {

        this.toolBar = toolBar;
        this.toolBar.setOnCloseRequest(event -> {

            this.hideToolbarAction();
            event.consume();
        });
    }

    public void setImageHelper(@NotNull ImageHelper imageHelper) {

        this.imageHelper = imageHelper;
        this.imageHelper.setSaveMenus(this.saveMenu, this.saveAsMenu);
        this.imageHelper.setHistoryMenus(this.undoMenu, this.redoMenu);
    }

    public void setMainStage(@NotNull Stage mainStage) {
        this.mainStage = mainStage;
    }
}
