package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.ui.component.ValidatingTextField;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class RoomCreateController {

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
    CheckBox gymCheckBox;
    @FXML
    CheckBox wifiCheckBox;
    @FXML
    CheckBox poolCheckBox;
    @FXML
    CheckBox parkingCheckBox;
    @FXML
    CheckBox spaCheckBox;
    @FXML
    CheckBox breakfastCheckBox;

    @FXML
    Button createRoomButton;
    private final String buttonText;
    private final RoomModel roomModel;
    private final Consumer<Room> action;

    private Map<CheckBox, SimpleBooleanProperty> checkBoxMap;
    private Map<ValidatingTextField, SimpleStringProperty> validatingTextFieldsMap;

    public RoomCreateController(RoomModel roomModel, Consumer<Room> action, String buttonText) {
        this.roomModel = roomModel;
        this.action = action;
        this.buttonText = buttonText;

    }

    @FXML
    private void initialize() {
        /*
        this.validatingTextFieldsMap = new HashMap<>(Map.of(
                roomModel.pricePerNightProperty(), pricePerNightValidatingTextField,
                roomModel.sizeInSqrMProperty(), sizeOfRoomValidatingTextField,
                roomModel.distanceFromCityCenterProperty(), distanceFromCityValidatingTextField,
                roomModel.distanceFromBeachProperty(), distanceFromBeachValidatingTextField,
                roomModel.roomNumberProperty(), roomNumberValidatingTextField
        ));
        this.checkBoxMap = new HashMap<>(Map.of(
                roomModel.getBoolPropertyForAmenity(Room.Amenity.GYM), gymCheckBox,
                roomModel.getBoolPropertyForAmenity(Room.Amenity.WIFI), wifiCheckBox,
                roomModel.getBoolPropertyForAmenity(Room.Amenity.POOL), poolCheckBox,
                roomModel.getBoolPropertyForAmenity(Room.Amenity.PARKING), parkingCheckBox,
                roomModel.getBoolPropertyForAmenity(Room.Amenity.SPA), spaCheckBox,
                roomModel.getBoolPropertyForAmenity(Room.Amenity.BREAKFAST), breakfastCheckBox
        ));
         */
        this.validatingTextFieldsMap = new HashMap<>(Map.of(
                pricePerNightValidatingTextField, roomModel.pricePerNightProperty(),
                sizeOfRoomValidatingTextField, roomModel.sizeInSqrMProperty(),
                distanceFromCityValidatingTextField, roomModel.distanceFromCityCenterProperty(),
                distanceFromBeachValidatingTextField, roomModel.distanceFromBeachProperty(),
                roomNumberValidatingTextField, roomModel.roomNumberProperty()
        ));

        this.checkBoxMap = new HashMap<>(Map.of(
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

        pricePerNightValidatingTextField.setValidation(string -> {
            try {
                new BigDecimal(string);
                return true;
            } catch (Exception e) {
                return false;
            }
        });

        sizeOfRoomValidatingTextField.setValidation(string -> {
            try { Integer.parseInt(string); return true; } catch (Exception e) { return false; }
        });

        distanceFromCityValidatingTextField.setValidation(string -> {
            try { new BigDecimal(string); return true; } catch (Exception e) { return false; }
        });

        distanceFromBeachValidatingTextField.setValidation(string -> {
            try { new BigDecimal(string); return true; } catch (Exception e) { return false; }
        });

        roomNumberValidatingTextField.setValidation(string -> {
            try { Integer.parseInt(string); return true; } catch (Exception e) { return false; }
        });

        /*
        pricePerNightValidatingTextField.textProperty().bindBidirectional(roomModel.pricePerNightProperty());
        sizeOfRoomValidatingTextField.textProperty().bindBidirectional(roomModel.sizeInSqrMProperty());
        distanceFromCityValidatingTextField.textProperty().bindBidirectional(roomModel.distanceFromCityCenterProperty());
        distanceFromBeachValidatingTextField.textProperty().bindBidirectional(roomModel.distanceFromBeachProperty());
        roomNumberValidatingTextField.textProperty().bindBidirectional(roomModel.roomNumberProperty());
        */

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

    public RoomModel getRoomModel() {
        return roomModel;
    }
}