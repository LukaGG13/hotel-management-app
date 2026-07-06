package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.UUID;

public class RoomModel {
    private static final Logger log = LoggerFactory.getLogger(RoomModel.class);

    private final ObjectProperty<Integer> numberOfBeds;
    private final SimpleStringProperty pricePerNight;
    private final SimpleStringProperty sizeInSqrM;
    private final SimpleStringProperty distanceFromCityCenter;
    private final SimpleStringProperty distanceFromBeach;
    private final SimpleStringProperty roomNumber;

    private UUID uuid;
    private ObservableMap<Room.Amenity, SimpleBooleanProperty> amenitiesMap;
    private SimpleStringProperty amenities;
    private final SimpleStringProperty id;

    private String amenityMapToString(ObservableMap<Room.Amenity, SimpleBooleanProperty> amenitiesMap) {
        return amenitiesMap.entrySet().stream()
                .filter(entry -> entry.getValue().get())
                .map(entry -> entry.getKey().name())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public RoomModel(Room room) {
        log.debug("Creating room model for room with id:{} beds:{}", room.getId(), room.getNumOfBeds());

        uuid = room.getId();
        id = new SimpleStringProperty(uuid == null ? "" : uuid.toString());
        numberOfBeds = new SimpleObjectProperty<>(room.getNumOfBeds());
        pricePerNight = new SimpleStringProperty(room.getPricePerNight() == null ? "0" : room.getPricePerNight().toString());
        sizeInSqrM = new SimpleStringProperty(room.getSizeInSqrM() == null ? "0" : room.getSizeInSqrM().toString());
        distanceFromCityCenter = new SimpleStringProperty(room.getDistanceFromCityCenter() == null ? "0" : room.getDistanceFromCityCenter().toString());
        distanceFromBeach = new SimpleStringProperty(room.getDistanceFromBeach() == null ? "0" : room.getDistanceFromBeach().toString());
        roomNumber = new SimpleStringProperty(room.getRoomNumber() == null ? "0" : room.getRoomNumber().toString());

        amenitiesMap = FXCollections.observableMap(new EnumMap<>(Room.Amenity.class));
        amenities = new SimpleStringProperty();
        for (var amenity: Room.Amenity.values()) {
            var simpleBooleanProperty = new SimpleBooleanProperty(room.getAmenities().contains(amenity));
            simpleBooleanProperty.addListener((Observable _) -> {
                String amenitiesString = amenityMapToString(amenitiesMap);
                amenities.setValue(amenitiesString);
            });
            amenitiesMap.put(amenity, simpleBooleanProperty);
        }
        amenities.setValue(amenityMapToString(amenitiesMap));
    }

    public RoomModel() {
        numberOfBeds = new SimpleObjectProperty<>(1);
        pricePerNight = new SimpleStringProperty("0");
        sizeInSqrM = new SimpleStringProperty("0");
        distanceFromCityCenter = new SimpleStringProperty("0");
        distanceFromBeach = new SimpleStringProperty("0");
        roomNumber = new SimpleStringProperty("101");

        amenitiesMap = FXCollections.observableMap(new EnumMap<>(Room.Amenity.class));
        amenities = new SimpleStringProperty();
        for (var amenity : Room.Amenity.values()) {
            var simpleBooleanProperty = new SimpleBooleanProperty(false);
            simpleBooleanProperty.addListener((Observable o) -> {
                String amenitiesString = amenityMapToString(amenitiesMap);
                log.debug("Amenities changed, new value: {}", amenitiesString);
                amenities.setValue(amenitiesString);
            });


            amenitiesMap.put(amenity, simpleBooleanProperty);
        }
        amenities.setValue(amenityMapToString(amenitiesMap));

        amenitiesMap.addListener((Observable o) -> {
            String amenitiesString = amenitiesMap.entrySet().stream()
                    .filter(entry -> entry.getValue().get())
                    .map(entry -> entry.getKey().name())
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            log.debug("Amenities changed, new value: {}", amenitiesString);
            amenities.setValue(amenitiesString);
        });

        id = new SimpleStringProperty("");
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds.get();
    }

    public ObjectProperty<Integer> numberOfBedsProperty() {
        return numberOfBeds;
    }

    public String getPricePerNight() {
        return pricePerNight.get();
    }

    public SimpleStringProperty pricePerNightProperty() {
        return pricePerNight;
    }

    public String getSizeInSqrM() {
        return sizeInSqrM.get();
    }

    public SimpleStringProperty sizeInSqrMProperty() {
        return sizeInSqrM;
    }

    public String getDistanceFromCityCenter() {
        return distanceFromCityCenter.get();
    }

    public SimpleStringProperty distanceFromCityCenterProperty() {
        return distanceFromCityCenter;
    }

    public String getDistanceFromBeach() {
        return distanceFromBeach.get();
    }

    public SimpleStringProperty distanceFromBeachProperty() {
        return distanceFromBeach;
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleBooleanProperty getBoolPropertyForAmenity(Room.Amenity amenity) {
        return amenitiesMap.get(amenity);
    }

    public SimpleStringProperty amenitiesProperty() {
        return amenities;
    }

    public Room toRoom() {
        if (uuid == null) uuid = UUID.randomUUID();

        var price = new BigDecimal(getPricePerNight());
        var numBeds = getNumberOfBeds();

        Room.RoomBuilder builder = new Room.RoomBuilder(uuid, numBeds, price);

        try {
            builder.sizeInSqrM(Integer.parseInt(getSizeInSqrM()));
            builder.distanceFromCityCenter(new BigDecimal(getDistanceFromCityCenter()));
            builder.distanceFromBeach(new BigDecimal(getDistanceFromBeach()));
            builder.roomNumber(Integer.parseInt(getRoomNumber()));
        } catch (NumberFormatException e) {
            log.error("Error wile trying to convert room model to room {}", e.getMessage());
        }

        for (var amenity : Room.Amenity.values()) {
            if (Boolean.TRUE.equals(amenitiesMap.get(amenity).getValue())) {
                log.debug("Adding amenity {}", amenity);
                builder.addAmenity(amenity);
            }
        }

        return builder.build();
    }

    @Override
    public String toString() {
        return "RoomModel{" +
                "numberOfBeds=" + numberOfBeds.get() +
                ", pricePerNight=" + pricePerNight.get() +
                ", roomNumber=" + roomNumber.get() +
                ", amenities=" + amenities +
                '}';
    }
}