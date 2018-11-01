package com.dusten.paint.components;

import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.Callable;

/**
 * @author Dusten Knull
 *
 * Component field for use in FXML that is based on the TextField component,
 * but only accepts and returns Integer values
 */
public class IntegerField extends TextField {

    private IntegerProperty value;
    private Callable<Void> onUpdate;

    private int maxValue;
    private int minValue;

    public IntegerField(@NamedArg("maxValue") int maxValue, @NamedArg("minValue") int minValue) {

        this.value = new SimpleIntegerProperty();
        this.maxValue = maxValue;
        this.minValue = minValue;

        this.setValue(0);

        /*
         * Whenever a key is typed, only accept characters in '0123456789'
         */
        this.addEventFilter(KeyEvent.KEY_TYPED, (keyEvent) -> {

            if(!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }

            try {
                this.onUpdate.call();
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        /*
         * Makes sure that the number value that was typed into the field
         * stays greater than or equal to MIN_VALUE and less than or
         * equal to MAX_VALUE
         */
        this.textProperty().addListener((observableValue, oldValue, newValue) -> {

            if(newValue == null || newValue.isEmpty())
                this.value.setValue(0);

            else if(this.minValue > Integer.valueOf(newValue) || Integer.valueOf(newValue) > this.maxValue)
                this.textProperty().setValue(oldValue);

            if(!this.textProperty().get().isEmpty())
                this.value.set(Integer.valueOf(this.textProperty().get()));
            else
                this.value.setValue(0);

            try {
                this.onUpdate.call();
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the field value within the specified constraints
     * of MAX_VALUE and MIN_VALUE
     *
     * @param newValue New value to set field to
     */
    public void setValue(int newValue) {

        if(newValue > this.maxValue || newValue < this.minValue)
            return;

        this.value.setValue(newValue);
        this.setText(String.valueOf(newValue));
    }

    /**
     * @return True when the field value is greater than or equal to MAX_VALUE, otherwise false
     */
    public boolean atMaxValue() {
        return this.value.getValue() >= this.maxValue;
    }

    /**
     * @return True when the field value is less than or equal to MIN_VALUE, otherwise false
     */
    public boolean atMinValue() {
        return this.value.getValue() <= this.minValue;
    }

    public void setOnUpdateCall(Callable<Void> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public int getValue() {
        return this.value.getValue();
    }
}
