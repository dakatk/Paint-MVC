package com.dusten.paint.helpers;

import com.dusten.paint.controllers.MenuController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.popup.ToolBarPopup;
import com.sun.istack.internal.NotNull;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Dusten Knull
 *
 * Menu component with a parent menubar that contains various submenus. This component class includes
 * defintions for what different menu options do (such as 'File -> Open') based on behaviors defined in other objects
 */
public class MenuHelper extends MenuBar {

    private MenuController controller;

    public MenuHelper() throws IOException {

        FXMLParser<MenuController, MenuBar> fxmlParser = new FXMLParser<>(FilesEnum.MENUHELPER_FXML);

        this.controller = fxmlParser.getController();
        this.getMenus().addAll(fxmlParser.getParent().getMenus());
    }

    public void setToolBar(@NotNull ToolBarPopup toolBar) {
        this.controller.setToolBar(toolBar);
    }

    public void setMainStage(@NotNull Stage mainStage) {
        this.controller.setMainStage(mainStage);
    }

    public void setImageHelper(@NotNull ImageHelper imageHelper) {
        this.controller.setImageHelper(imageHelper);
    }
}
