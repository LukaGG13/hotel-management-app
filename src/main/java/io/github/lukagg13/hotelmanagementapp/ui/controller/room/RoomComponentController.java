package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RoomComponentController extends VBox {

    @FXML private Label roomNumberLabel;
    @FXML private Label priceLabel;
    @FXML private Label bedsLabel;
    @FXML private Label sizeLabel;
    @FXML private Label centerDistanceLabel;
    @FXML
    private Label beachDistanceLabel;
    @FXML private Label amenitiesLabel;

    private final RoomModel roomModel;

    public RoomComponentController(RoomModel roomModel) {
        this.roomModel = roomModel;

        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/room-component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load room-component.fxml", e);
        }
    }

    @FXML
    public void initialize() {
        // Direct architectural string and property bindings to keep data UI synced
        roomNumberLabel.textProperty().bind(roomModel.roomNumberProperty().map(num -> "Room " + num));
        priceLabel.textProperty().bind(roomModel.pricePerNightProperty().map(price -> "E" + price + " / night"));
        bedsLabel.textProperty().bind(roomModel.numberOfBedsProperty().map(beds -> "Beds: " + beds));
        sizeLabel.textProperty().bind(roomModel.sizeInSqrMProperty().map(size -> "Size: " + size + " m²"));
        centerDistanceLabel.textProperty().bind(roomModel.distanceFromCityCenterProperty().map(dist -> "Center: " + dist + " km"));
        beachDistanceLabel.textProperty().bind(roomModel.distanceFromBeachProperty().map(dist -> "Beach: " + dist + " km"));

        // Binds the text layout string that computes directly from your internal EnumMap updates
        amenitiesLabel.textProperty().bind(roomModel.amenitiesProperty().map(amenityStr ->
                amenityStr.isEmpty() ? "Amenities: None" : "Amenities: " + amenityStr.replace(",", ", ")
        ));
    }

    public RoomModel getRoomModel() {
        return roomModel;
    }
}