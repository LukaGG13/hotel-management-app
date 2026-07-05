package io.github.lukagg13.hotelmanagementapp.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

/**
 * A custom {@link TextField} that can validate input.
 */
public class ValidatingTextField extends TextField {

    private Predicate<String> validation = _ -> true;
    private final BooleanProperty valid = new SimpleBooleanProperty(true);

    /**
     * Returns a new instance of the {@link ValidatingTextField}.
     */
    public ValidatingTextField() {
        super();
        textProperty().addListener((_, _, _) -> validate());
    }

    /**
     * Sets the validation that the {@link ValidatingTextField} will use.
     * @param validation A {@link Predicate} that will be used for validation.
     */
    public void setValidation(Predicate<String> validation) {
        this.validation = validation != null ? validation : _ -> true;
        validate();
    }

    /**
     * Check if the text in the {@link ValidatingTextField} is valid.
     * @return True if its valid false if it isn't.
     */
    public boolean isValid() {
        return valid.get();
    }

    /**
     * Gets the {@link BooleanProperty} used by {@link ValidatingTextField}.
     * @return The {@link BooleanProperty} used by {@link ValidatingTextField}.
     */
    public BooleanProperty validProperty() {
        return valid;
    }

    /**
     * Used to check if the text of the {@link ValidatingTextField} matches the {@link Predicate}.
     * If it isn't valid the border color will be changed to red.
     * And valid will return false.
     */
    private void validate() {
        boolean result = validation.test(getText());
        valid.set(result);

        if (result) {
            setStyle("");
        } else {
            setStyle("-fx-border-color: red;");
        }
    }
}
