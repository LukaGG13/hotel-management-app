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


/**
 * Controller class used for {@link Guest} creating and editing.
 */
public final class GuestCreateController {

    private static final Logger log = LoggerFactory.getLogger(GuestCreateController.class);

    @FXML
    private ValidatingTextField nameTextField;
    @FXML
    private Spinner<Integer> ageSpinner;
    @FXML
    private Button confirmButton;
    private boolean confirmed = false;


    private final GuestModel guestModel;

    /**
     * Return a new instance of the {@link GuestCreateController}.
     * @param guestModel The {@link GuestModel} that will be bound to the ui.
     */
    public GuestCreateController(GuestModel guestModel) {
        this.guestModel = guestModel;

    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100);
        spinnerValueFactory.setValue(20);
        ageSpinner.setValueFactory(spinnerValueFactory);

        nameTextField.setValidation(string -> !string.isBlank());

        nameTextField.textProperty().bindBidirectional(guestModel.nameProperty());
        ageSpinner.getValueFactory().valueProperty().bindBidirectional(guestModel.ageProperty());

        confirmButton.setOnAction(_ -> {
            log.info("Confirm button clicked");
            confirmed = false;
            if (nameTextField.isValid()) {
                confirmed = true;
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * Returns the {@link GuestModel} that was passed to the constructor.
     * @return {@link GuestModel} if the user has confirmed the change else return null.
     */
    public GuestModel getGuestModel() {
        if (confirmed) {
            return guestModel;
        } else {
            return null;
        }
    }
}
