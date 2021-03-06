package com.dusten.paint.main;

import com.dusten.paint.controllers.PaintController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.popup.MessagePopup;
import com.dusten.paint.popup.ToolBarPopup;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Dusten Knull
 *
 * The main class for this project, also doubles as the controller for
 * the main FXML file (specified as 'PaintApp.fxml')
 */
public class PaintApp extends Application {

    public static final String TITLE = "Pain(t)";
    private static final int MIN_WIDTH = 200;
    private static final int MIN_HEIGHT = 200;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLParser<PaintController, Parent> fxmlParser = new FXMLParser<>(FilesEnum.MAIN_FXML);
        PaintController controller = fxmlParser.getController();

        Scene scene = new Scene(fxmlParser.getParent(), WIDTH, HEIGHT);
        ToolBarPopup toolBarPopup = new ToolBarPopup();

        scene.addEventHandler(MouseEvent.ANY, (event) -> {

            toolBarPopup.toBack();
            toolBarPopup.getSettingsWindow().toBack();
            primaryStage.requestFocus();
        });

        // TODO add scrollbars if canvas size exceeds window size
        primaryStage.setTitle(TITLE + " - <Untitled>");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        primaryStage.setScene(scene);
        toolBarPopup.setParentScene(scene);

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
            controller.checkScrollbarVisibility(primaryStage.getWidth(), primaryStage.getHeight())
        );

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
            controller.checkScrollbarVisibility(primaryStage.getWidth(), primaryStage.getHeight())
        );

        primaryStage.setOnCloseRequest(event -> {

            if(controller.getCanvasHelper().hasNotSaved()) {

                // Queries the user to save on close if they have not saved recently
                ButtonType saveQuery = MessagePopup.showAsQueryAndWait("Save before closing?");

                if(saveQuery.equals(ButtonType.CANCEL))
                    event.consume();

                else {

                    if(saveQuery.equals(ButtonType.OK))
                        controller.getCanvasHelper().saveImage();

                    toolBarPopup.close();
                    toolBarPopup.getSettingsWindow().close();
                }
            }
            else {

                toolBarPopup.close();
                toolBarPopup.getSettingsWindow().close();
            }
        });

        primaryStage.setOnHidden(event -> {

            toolBarPopup.hide();
            toolBarPopup.getSettingsWindow().hide();
        });

        primaryStage.setOnShown(event -> {

            toolBarPopup.show();
            toolBarPopup.getSettingsWindow().show();
        });

        controller.setMainStage(primaryStage);
        controller.setToolBar(toolBarPopup);

        primaryStage.show();
        toolBarPopup.show();

        this.setLayoutUI(toolBarPopup, toolBarPopup.getSettingsWindow(), primaryStage);

        System.out.println(primaryStage.getWidth()+","+primaryStage.getHeight());
    }

    private void setLayoutUI(Stage leftWindow, Stage topWindow, Stage centerWindow) {

        topWindow.setX(0.0);
        topWindow.setY(24.0);
        topWindow.setWidth(Screen.getPrimary().getVisualBounds().getWidth());

        leftWindow.setX(0.0);
        leftWindow.setY(topWindow.getHeight() + 25.0);

        centerWindow.setX(leftWindow.getWidth() + 25.0);
        centerWindow.setY(topWindow.getHeight() + 40.0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
