package popup;

import com.dusten.paint.components.IntegerField;
import com.dusten.paint.controllers.ResizeController;
import com.dusten.paint.enums.FilesEnum;
import com.dusten.paint.fxml.FXMLParser;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class ResizePopupTest extends ApplicationTest {

    private ResizeController controller;
    private Parent parent;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLParser<ResizeController, Parent> fxmlParser = new FXMLParser<>(FilesEnum.RESIZEPOPUP_FXML);

        this.controller = fxmlParser.getController();
        this.parent = fxmlParser.getParent();

        Scene scene = new Scene(this.parent);

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

        Button widthInc = lookup("#widthInc").queryAs(Button.class);
        Button widthDec = lookup("#widthDec").queryAs(Button.class);
        Button heightInc = lookup("#heightInc").queryAs(Button.class);
        Button heightDec = lookup("#heightDec").queryAs(Button.class);

        IntegerField widthInput = lookup("#widthInput").queryAs(IntegerField.class);
        IntegerField heightInput = lookup("#heightInput").queryAs(IntegerField.class);

        assertNotNull(widthInc);
        assertNotNull(widthDec);
        assertNotNull(heightInc);
        assertNotNull(heightDec);

        assertNotNull(widthInput);
        assertNotNull(heightInput);
    }

    @Test
    public void testButtons() {

        this.controller.setFieldValues(100, 100);

        Button widthInc = lookup("#widthInc").queryAs(Button.class);
        Button widthDec = lookup("#widthDec").queryAs(Button.class);
        Button heightInc = lookup("#heightInc").queryAs(Button.class);
        Button heightDec = lookup("#heightDec").queryAs(Button.class);

        IntegerField widthInput = lookup("#widthInput").queryAs(IntegerField.class);
        IntegerField heightInput = lookup("#heightInput").queryAs(IntegerField.class);

        widthInc.fire();
        assertEquals(widthInput.getValue(), 101);

        widthDec.fire();
        assertEquals(widthInput.getValue(), 100);

        heightInc.fire();
        assertEquals(heightInput.getValue(), 101);

        heightDec.fire();
        assertEquals(heightInput.getValue(), 100);
    }
}
