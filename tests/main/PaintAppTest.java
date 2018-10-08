package main;

import com.dusten.paint.controllers.PaintController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import com.dusten.paint.helpers.CanvasHelper;
import com.dusten.paint.helpers.MenuHelper;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertNotNull;

public class PaintAppTest extends ApplicationTest {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private PaintController controller;
    private Parent parent;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLParser<PaintController, Parent> fxmlParser = new FXMLParser<>(FilesEnum.MAIN_FXML);

        this.controller = fxmlParser.getController();
        this.parent = fxmlParser.getParent();

        Scene scene = new Scene(this.parent, WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Before
    public void setup() {}

    @After
    public void teardown() throws Exception {

        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testLoadFXML() {

        assertNotNull(this.controller);
        assertNotNull(this.parent);
    }

    @Test
    public void testComponentsExist() {

        CanvasHelper canvasHelper = lookup("#canvasHelper").queryAs(CanvasHelper.class);
        MenuHelper menuHelper = lookup("#menuHelper").queryAs(MenuHelper.class);

        assertNotNull(canvasHelper);
        assertNotNull(menuHelper);
    }
}
