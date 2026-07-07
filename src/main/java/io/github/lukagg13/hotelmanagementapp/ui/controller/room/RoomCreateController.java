package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.ui.component.ValidatingTextField;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Controller used for creating rooms.
 */
public final class RoomCreateController {

    private static final String POSITIVE_INTEGER_REGEX = "^[1-9]\\d*$";
    private static final String POSITIVE_DECIMAL_REGEX = "^[1-9]\\d*(\\.\\d+)?$|^0\\.\\d*[1-9]\\d*$";
    private static final Logger log = LoggerFactory.getLogger(RoomCreateController.class);

    @FXML
    private Spinner<Integer> numberOfBedsSpinner;
    @FXML
    private ValidatingTextField pricePerNightValidatingTextField;
    @FXML
    private ValidatingTextField sizeOfRoomValidatingTextField;
    @FXML
    private ValidatingTextField distanceFromCityValidatingTextField;
    @FXML
    private ValidatingTextField distanceFromBeachValidatingTextField;
    @FXML
    private ValidatingTextField roomNumberValidatingTextField;

    @FXML
    private CheckBox gymCheckBox;
    @FXML
    private CheckBox wifiCheckBox;
    @FXML
    private CheckBox poolCheckBox;
    @FXML
    private CheckBox parkingCheckBox;
    @FXML
    private CheckBox spaCheckBox;
    @FXML
    private CheckBox breakfastCheckBox;
    @FXML
    private Button createRoomButton;

    private final String buttonText;
    private final RoomModel roomModel;
    private final Consumer<Room> action;

    /**
     * Returns a new instance of the {@link RoomCreateController}.
     * @param roomModel The model which will be bound to the ui.
     * @param action The {@link Consumer} action that happens when the button is pressed so it can be used for updating and creating.
     * @param buttonText The {@link String} that will be used as button text.
     */
    public RoomCreateController(RoomModel roomModel, Consumer<Room> action, String buttonText) {
        this.roomModel = roomModel;
        this.action = action;
        this.buttonText = buttonText;

    }

    /**
     * Initialize the ui for JAVAFX.
     */
    @FXML
    private void initialize() {
        final var validatingTextFieldsMap = new HashMap<>(Map.of(
                pricePerNightValidatingTextField, roomModel.pricePerNightProperty(),
                sizeOfRoomValidatingTextField, roomModel.sizeInSqrMProperty(),
                distanceFromCityValidatingTextField, roomModel.distanceFromCityCenterProperty(),
                distanceFromBeachValidatingTextField, roomModel.distanceFromBeachProperty(),
                roomNumberValidatingTextField, roomModel.roomNumberProperty()
        ));

        final var checkBoxMap = new HashMap<>(Map.of(
                gymCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.GYM),
                wifiCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.WIFI),
                poolCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.POOL),
                parkingCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.PARKING),
                spaCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.SPA),
                breakfastCheckBox, roomModel.getBoolPropertyForAmenity(Room.Amenity.BREAKFAST)
        ));

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);
        spinnerValueFactory.setValue(1);
        numberOfBedsSpinner.setValueFactory(spinnerValueFactory);

        pricePerNightValidatingTextField.setValidation(string ->
            string != null && string.matches(POSITIVE_DECIMAL_REGEX)
        );

        sizeOfRoomValidatingTextField.setValidation(string ->
                string != null && string.matches(POSITIVE_INTEGER_REGEX)
        );

        distanceFromCityValidatingTextField.setValidation(string ->
                string != null && string.matches(POSITIVE_DECIMAL_REGEX)
        );

        distanceFromBeachValidatingTextField.setValidation(string ->
                string != null && string.matches(POSITIVE_DECIMAL_REGEX)
        );

        roomNumberValidatingTextField.setValidation(string ->
                string != null && string.matches(POSITIVE_INTEGER_REGEX)
        );

        numberOfBedsSpinner.getValueFactory().valueProperty().bindBidirectional(roomModel.numberOfBedsProperty());

        for(var mapPair : validatingTextFieldsMap.entrySet()) {
           mapPair.getKey().textProperty().bindBidirectional(mapPair.getValue());
        }

        for(var mapPair : checkBoxMap.entrySet()) {
            mapPair.getKey().selectedProperty().bindBidirectional(mapPair.getValue());
        }

        createRoomButton.textProperty().setValue(buttonText);
        createRoomButton.setOnAction(_ -> {
            log.info("Room action clicked");

            if (validatingTextFieldsMap.keySet().stream().allMatch(ValidatingTextField::isValid)) {
                action.accept(roomModel.toRoom());
                Stage stage = (Stage) createRoomButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * Returns the {@link RoomModel} passed to the constructor.
     * @return The {@link RoomModel}.
     */
    public RoomModel getRoomModel() {
        return roomModel;
    }
}