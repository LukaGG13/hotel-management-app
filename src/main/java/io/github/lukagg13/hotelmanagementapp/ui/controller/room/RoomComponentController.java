package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RoomComponentController extends VBox {

    private static final Logger log = LoggerFactory.getLogger(RoomComponentController.class);

    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label bedsLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private Label centerDistanceLabel;
    @FXML
    private Label beachDistanceLabel;
    @FXML
    private Label amenitiesLabel;

    private final RoomModel roomModel;

    public RoomComponentController(RoomModel roomModel) {
        this.roomModel = roomModel;

        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/room-component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            roomNumberLabel.textProperty().bind(roomModel.roomNumberProperty().map(num -> "Room " + num));
            priceLabel.textProperty().bind(roomModel.pricePerNightProperty().map(price -> price + "€"));
            bedsLabel.textProperty().bind(roomModel.numberOfBedsProperty().map(beds -> "Beds: " + beds));
            sizeLabel.textProperty().bind(roomModel.sizeInSqrMProperty().map(size -> "Size: " + size + " m²"));
            centerDistanceLabel.textProperty().bind(roomModel.distanceFromCityCenterProperty().map(dist -> "Center: " + dist + " km"));
            beachDistanceLabel.textProperty().bind(roomModel.distanceFromBeachProperty().map(dist -> "Beach: " + dist + " km"));

            amenitiesLabel.textProperty().bind(roomModel.amenitiesProperty().map(amenityStr ->
                    amenityStr.isEmpty() ? "Amenities: None" : "Amenities: " + amenityStr.replace(",", ", ")
            ));
        } catch (IOException e) {
            log.error("Error loading Room component {}", e.getMessage());
        }
    }

    public RoomModel getRoomModel() {
        return roomModel;
    }
}