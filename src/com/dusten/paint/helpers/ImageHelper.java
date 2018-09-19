package com.dusten.paint.helpers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.ImageController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.main.PaintApp;
import com.dusten.paint.popup.MessagePopup;
import com.sun.istack.internal.NotNull;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dusten Knull
 *
 * An ImageView component with helper functions for loading and saving an image in
 * the 'File' menu
 */
public class ImageHelper extends StackPane {

    private ImageController controller;
    private Stage mainStage;

    private FileChooser fileChooser;
    private String formatName;
    private File opened;

    private MenuItem saveMenu;
    private MenuItem saveAsmenu;

    private MenuItem undoMenu;
    private MenuItem redoMenu;

    public ImageHelper() throws IOException {

        FXMLParser<ImageController, StackPane> fxmlParser = new FXMLParser<>(FilesEnum.IMAGEHELPER_FXML);
        StackPane parent = fxmlParser.getParent();

        this.controller = fxmlParser.getController();
        this.controller.setParent(this);

        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Select an image file...");
        this.fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        this.opened = null;
        this.formatName = null;

        this.generateExtensionFilters();

        this.controller.setCanvasSize(parent.getPrefWidth(), parent.getPrefHeight());
        this.getChildren().addAll(parent.getChildren());
    }

    /**
     * Procedural generation of filters for image file extension names that can
     * can be loaded with 'File -> Open' from the in-app menu
     */
    private void generateExtensionFilters() {

        String[] saveFormatOptions = ImageIO.getWriterFormatNames();
        List<Integer> used = new ArrayList<>(saveFormatOptions.length);

        Arrays.sort(saveFormatOptions);

        for(int i = 0; i < saveFormatOptions.length; i ++) {

            if(used.contains(i))
                continue;

            List<String> extensions = new ArrayList<>();
            extensions.add("*." + saveFormatOptions[i]);

            for(int j = i + 1; j < saveFormatOptions.length; j ++) {

                if(saveFormatOptions[i].toLowerCase().equals(saveFormatOptions[j].toLowerCase())) {

                    extensions.add("*." + saveFormatOptions[j]);
                    used.add(j);
                }
            }

            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(saveFormatOptions[i].toUpperCase(), extensions);
            this.fileChooser.getExtensionFilters().add(filter);
        }
    }

    /**
     * Controls what happens when 'File -> Load' is selected from the
     * application's menu
     */
    public void loadImage() {

        if(this.mainStage == null)
            return;

        File loadFrom = this.fileChooser.showOpenDialog(this.mainStage);

        if(this.fileChooser.getSelectedExtensionFilter() == null)
            return;

        this.formatName = this.fileChooser.getSelectedExtensionFilter().getDescription();

        if(loadFrom != null) {

            this.opened = loadFrom;
            this.mainStage.setTitle(PaintApp.TITLE + " - " + this.opened.getName() + "*");
            this.controller.setImage(new Image(this.opened.toURI().toString()));

            this.updateSavedState(true);
        }
    }

    /**
     * Controls what happens when 'File -> Save' is selected from the
     * application's menu
     */
    public void saveImage() {

        if(this.mainStage == null)
            return;

        // if save is called and no file has been opened:
        if(this.opened == null) {

            this.opened = this.fileChooser.showSaveDialog(this.mainStage);

            if(this.fileChooser.getSelectedExtensionFilter() != null)
                this.formatName = this.fileChooser.getSelectedExtensionFilter().getDescription();

            if(this.opened != null) {

                if(!this.opened.exists()) {

                    try {
                        if(!this.opened.createNewFile())
                            MessagePopup.showAsError("File could not be created");

                    } catch(Exception e) {
                        MessagePopup.showAsError("File could not be created");
                    }
                }
            }
            else return;
        }

        WritableImage baseImage = this.controller.getImage();
        RenderedImage rawImage;

        try {
            // write the image back to the file (current best way to
            // achieve this is with Swing's 'ImageIO' class)
            rawImage = SwingFXUtils.fromFXImage(baseImage, null);
            ImageIO.write(rawImage, this.formatName.toLowerCase(), this.opened);

            this.controller.setSaved(true);

        } catch(Exception e) {
            MessagePopup.showAsError("Unable to write to file '" + this.opened.getName() + "'");
        }
    }

    /**
     * Controls what happens when 'File -> Save As' is selected from the
     * application's menu
     */
    public void saveImageAs() {

        if(this.mainStage == null)
            return;

        File saveTo = this.fileChooser.showSaveDialog(this.mainStage);

        if(this.fileChooser.getSelectedExtensionFilter() != null)
            this.formatName = this.fileChooser.getSelectedExtensionFilter().getDescription();

        if(saveTo != null) {

            if(!saveTo.exists()) {

                try {
                    if(saveTo.createNewFile()) {

                        this.opened = saveTo;
                        this.saveImage();
                    }
                    else MessagePopup.showAsError("File could not be created");

                } catch(Exception e) {
                    MessagePopup.showAsError("File could not be created");
                }
                
            } else {

                this.opened = saveTo;
                this.saveImage();
            }
        }
    }

    /**
     * Tells the application whether or not it should show
     * the state as 'saved' or not, letting the user know
     * when they can and can't save the current canvas
     *
     * @param saved Whether or not to set the state to 'saved'
     */
    public void updateSavedState(boolean saved) {

        // default string to show when no file is loaded
        String openedName = "<Untitled>";

        if(this.opened != null)
            openedName = this.opened.getName();

        String title = PaintApp.TITLE + " - " + openedName;

        if(!saved)
            this.mainStage.setTitle(title + "*");
        else
            this.mainStage.setTitle(title);

        if(this.saveMenu != null && this.saveAsmenu != null) {

            this.saveMenu.setDisable(saved);
            this.saveAsmenu.setDisable(saved);
        }

        if(this.redoMenu != null && this.undoMenu != null) {

            this.undoMenu.setDisable(this.controller.getCanvas().isUndoDisabled());
            this.redoMenu.setDisable(this.controller.getCanvas().isRedoDisabled());
        }
    }

    /**
     * Set the MenuItem object references for the Save and Save-As menus. This is
     * for the purpose of disabling/enabling these menus when the save status of
     * the application is updated
     *
     * @param saveMenu 'Save' MenuItem object
     * @param saveAsMenu 'Save-As' MenuItem object
     */
    public void setSaveMenus(@NotNull MenuItem saveMenu, @NotNull MenuItem saveAsMenu) {

        this.saveMenu = saveMenu;
        this.saveAsmenu = saveAsMenu;
    }

    /**
     * Set the MenuItem object references for the Undo and Redo menus. This is
     * for the purpose of disabling/enabling these menus when the save status of
     * the application is updated
     *
     * @param undoMenu 'Undo' MenuItem object
     * @param redoMenu 'Redo' MenuItem object
     */
    public void setHistoryMenus(@NotNull MenuItem undoMenu, @NotNull MenuItem redoMenu) {

        this.undoMenu = undoMenu;
        this.redoMenu = redoMenu;
    }

    public void undoEdit() {
        this.controller.getCanvas().undoEdit();
    }

    public void redoEdit() {
        this.controller.getCanvas().redoEdit();
    }

    public void setMainStage(@NotNull Stage mainStage) {
        this.mainStage = mainStage;
    }

    public boolean hasNotSaved() {
        return !this.controller.isSaved();
    }

    public ImageController getController() {
        return this.controller;
    }

    public DrawableCanvas getCanvas() {
        return this.controller.getCanvas();
    }
}
