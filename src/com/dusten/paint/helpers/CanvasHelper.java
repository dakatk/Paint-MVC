package com.dusten.paint.helpers;

import com.dusten.paint.components.DrawableCanvas;
import com.dusten.paint.controllers.CanvasController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.main.PaintApp;
import com.dusten.paint.popup.MessagePopup;
import com.sun.istack.internal.NotNull;
import javafx.embed.swing.SwingFXUtils;
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
public class CanvasHelper extends StackPane {

    private CanvasController controller;
    private Stage mainStage;

    private FileChooser fileChooser;
    private String formatName;
    private File opened;

    private StatusSet<Boolean> status;

    public CanvasHelper() throws IOException {

        FXMLParser<CanvasController, StackPane> fxmlParser = new FXMLParser<>(FilesEnum.IMAGEHELPER_FXML);
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
     *
     * @param saved
     */
    public void updateSaveStatus(boolean saved) {

        // default string to show when no file is loaded
        String openedName = "<Untitled>";

        if(this.opened != null)
            openedName = this.opened.getName();

        String title = PaintApp.TITLE + " - " + openedName;

        if(!saved)
            this.mainStage.setTitle(title + "*");
        else
            this.mainStage.setTitle(title);

        this.status.set(saved);
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

            if(this.status != null)
                this.status.set(true);
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

    public void copySelectedArea() {
        this.controller.getCanvas().copySelectedArea(false);
    }

    public void cutSelectedArea() {
        this.controller.getCanvas().copySelectedArea(true);
    }

    public void pasteClipboard() {
        this.controller.getCanvas().pasteClipboard();
    }

    public void clearCanvas() {
        this.controller.getCanvas().clearAll();
    }

    public void selectAll() {
        this.controller.getCanvas().selectAll();
    }

    public void undoEdit() {
        this.controller.getCanvas().undoEdit();
    }

    public void redoEdit() {
        this.controller.getCanvas().redoEdit();
    }

    public void setUpdateStatusCall(@NotNull StatusSet<Boolean> status) {
        this.status = status;
    }

    public void setMainStage(@NotNull Stage mainStage) {
        this.mainStage = mainStage;
    }

    public boolean hasNotSaved() {
        return !this.controller.isSaved();
    }

    public CanvasController getController() {
        return this.controller;
    }

    public DrawableCanvas getCanvas() {
        return this.controller.getCanvas();
    }

}
