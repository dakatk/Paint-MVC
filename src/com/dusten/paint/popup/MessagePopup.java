package com.dusten.paint.popup;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Dusten Knull
 *
 * Static class that contains Alert objects that are used to show errors,
 * warnings, and basic info as popup dialogue windows
 */
public class MessagePopup {

    private static final Alert error = new Alert(Alert.AlertType.ERROR);
    private static final Alert query = new Alert(Alert.AlertType.NONE, null,
            ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

    static {
        /*
         * Extra setup for all the static objects that
         * doesn't take place in their constructors
         */
        error.setHeaderText(null);
        query.setHeaderText(null);

        error.setTitle("Error");
        query.setTitle("Confirm");
    }

    /**
     * Displays popup error popup for user
     * @param message Message to show in error dialog box
     */
    public static void showAsError(String message) {

        error.setContentText(message);
        error.show();
    }

    /**
     * Displays popup query dialog box and then waits for user to
     * select an option ('Yes', 'No', 'Cancel, or 'x')
     *
     * @param message Message to show in confirm dialog box
     * @return True if 'Yes' button is pressed, false if 'No' is pressed, null otherwise
     */
    public static ButtonType showAsQueryAndWait(String message) {

        query.setContentText(message);
        return query.showAndWait().orElse(ButtonType.CANCEL);
    }
}
