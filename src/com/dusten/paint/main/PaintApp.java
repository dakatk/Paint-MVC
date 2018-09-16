package com.dusten.paint.main;

import com.dusten.paint.controllers.PaintController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.popup.MessagePopup;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
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
        ToolBar toolBar = new ToolBar();

        PaintController controller = fxmlParser.getController();

        // TODO add scrollbars if canvas size exceeds window size
        primaryStage.setTitle(TITLE + " - <Untitled>");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setScene(new Scene(fxmlParser.getParent(), WIDTH, HEIGHT));
        primaryStage.setOnCloseRequest(event -> {

            if(controller.getImageHelper().hasNotSaved()) {

                // Queries the user to save on close if they have not saved recently
                ButtonType saveQuery = MessagePopup.showAsQueryAndWait("Save before closing?");

                if(saveQuery.equals(ButtonType.CANCEL)) {

                    event.consume();
                    return;
                }

                else if(saveQuery.equals(ButtonType.OK))
                    controller.getImageHelper().saveImage();
            }
            toolBar.close();
            // Platform.exit();
        });

        controller.setMainStage(primaryStage);
        controller.setToolBar(toolBar);

        primaryStage.show();
        toolBar.showRelativeTo(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
