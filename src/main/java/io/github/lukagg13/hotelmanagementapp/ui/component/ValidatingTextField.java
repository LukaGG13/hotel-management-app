package io.github.lukagg13.hotelmanagementapp.ui.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class ValidatingTextField extends TextField {

    private Predicate<String> validation = _ -> true;
    private final BooleanProperty valid = new SimpleBooleanProperty(true);

    public ValidatingTextField() {
        super();

        textProperty().addListener((obs, oldText, newText) -> validate());
    }

    public void setValidation(Predicate<String> validation) {
        this.validation = validation != null ? validation : _ -> true;
        validate();
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

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
