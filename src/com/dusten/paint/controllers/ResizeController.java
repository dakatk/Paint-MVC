package com.dusten.paint.controllers;

import com.dusten.paint.components.IntegerField;
import com.dusten.paint.popup.ResizePopup;
import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dusten Knull
 *
 * Controller for ResizePopup.fxml
 */
public class ResizeController implements Initializable {

    @FXML private IntegerField widthInput;
    @FXML private IntegerField heightInput;

    @FXML private Button widthInc;
    @FXML private Button widthDec;
    @FXML private Button heightInc;
    @FXML private Button heightDec;

    private ResizePopup parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // update button enabled status when width input is updated
        // without the use of either button:
        this.widthInput.setOnUpdateCall(() -> {

            this.widthDec.setDisable(this.widthInput.atMinValue());
            this.widthInc.setDisable(this.widthInput.atMaxValue());

            return null;
        });

        // update button enabled status when height input is updated
        // without the use of either button:
        this.heightInput.setOnUpdateCall(() -> {

            this.heightDec.setDisable(this.heightInput.atMinValue());
            this.heightInc.setDisable(this.heightInput.atMaxValue());

            return null;
        });

        // properly resize width buttons:
        this.widthInc.setPrefWidth(this.widthInc.getHeight());
        this.widthDec.setPrefWidth(this.widthDec.getHeight());

        // properly resize height buttons:
        this.heightInc.setPrefWidth(this.heightInc.getHeight());
        this.heightDec.setPrefWidth(this.heightDec.getHeight());
    }

    /**
     * Increases the value of the width input field by 1
     */
    @FXML
    private void incrementWidth() {

        int canvasWidth = this.widthInput.getValue();
        this.widthInput.setValue(canvasWidth + 1);

        if(this.widthDec.isDisabled())
            this.widthDec.setDisable(false);

        if(this.widthInput.atMaxValue())
            this.widthInc.setDisable(true);
    }

    /**
     * Decreases the value of the width input field by 1
     */
    @FXML
    private void decrementWidth() {

        int canvasWidth = this.widthInput.getValue();
        this.widthInput.setValue(canvasWidth - 1);

        if(this.widthInc.isDisabled())
            this.widthInc.setDisable(false);

        if(this.widthInput.atMinValue())
            this.widthDec.setDisable(true);
    }

    /**
     * Increases the value of the height input field by 1
     */
    @FXML
    private void incrementHeight() {

        int canvasHeight = this.heightInput.getValue();
        this.heightInput.setValue(canvasHeight + 1);

        if(this.heightDec.isDisabled())
            this.heightDec.setDisable(false);

        if(this.heightInput.atMaxValue())
            this.heightInc.setDisable(true);
    }

    /**
     * Decreases the value of the height input field by 1
     */
    @FXML
    private void decrementHeight() {

        int canvasHeight = this.heightInput.getValue();
        this.heightInput.setValue(canvasHeight - 1);

        if(this.heightInc.isDisabled())
            this.heightInc.setDisable(false);

        if(this.heightInput.atMinValue())
            this.heightDec.setDisable(true);
    }

    /**
     * Closes the resize popup window and applies changes
     */
    @FXML
    private void confirmAction() {

        int canvasWidth = this.widthInput.getValue();
        int canvasHeight = this.heightInput.getValue();

        this.parent.setCanvasSize(canvasWidth, canvasHeight);
        this.parent.close();
    }

    /**
     * Closes the resize popup window without applying changes
     */
    @FXML
    private void cancelAction() {
        this.parent.close();
    }

    /**
     * Sets the values in both the width and height input fields
     *
     * @param widthValue New value to set the width input field to
     * @param heightValue New valut to set the height input field to
     */
    public void setFieldValues(int widthValue, int heightValue) {

        this.widthInput.setValue(widthValue);
        this.heightInput.setValue(heightValue);
    }

    public void setParent(@NotNull ResizePopup parent) {
        this.parent = parent;
    }
}
