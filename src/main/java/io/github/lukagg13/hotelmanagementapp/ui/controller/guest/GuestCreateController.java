package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.ui.component.ValidatingTextField;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class GuestCreateController {

    private static final Logger log = LoggerFactory.getLogger(GuestCreateController.class);

    @FXML
    private ValidatingTextField nameTextField;

    @FXML
    private Spinner<Integer> ageSpinner;

    @FXML
    private Button actionButton;
    private final String buttonText;

    private final GuestModel guestModel;
    private final Consumer<Guest> action;

    public GuestCreateController(GuestModel guestModel, Consumer<Guest> action, String buttonText) {
        this.guestModel = guestModel;
        this.action = action;
        this.buttonText = buttonText;
    }

    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100);
        spinnerValueFactory.setValue(20);
        ageSpinner.setValueFactory(spinnerValueFactory);

        nameTextField.setValidation(string -> !string.isBlank());

        nameTextField.textProperty().bindBidirectional(guestModel.nameProperty());
        ageSpinner.getValueFactory().valueProperty().bindBidirectional(guestModel.ageProperty());

        actionButton.textProperty().setValue(buttonText);
        actionButton.setOnAction(_ ->
        {
            log.info("Action button clicked");
            if (nameTextField.isValid()) {
                action.accept(guestModel.toGuest());
                Stage stage = (Stage) actionButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    public GuestModel getGuestModel() {
        return guestModel;
    }
}
