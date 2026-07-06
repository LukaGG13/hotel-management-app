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

/**
 * Class used to display the {@link RoomModel}'s in the app.
 */
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

    /**
     * Turns the amenityMap to a string.
     * @param amenitiesMap The amenity map to be turned in to the string.
     * @return The result {@link String}.
     */
    private String amenityMapToString(ObservableMap<Room.Amenity, SimpleBooleanProperty> amenitiesMap) {
        return amenitiesMap.entrySet().stream()
                .filter(entry -> entry.getValue().get())
                .map(entry -> entry.getKey().name())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    /**
     * Creates a new instance of {@link RoomModel} from an existing {@link RoomModel}.
     * @param room the {@link Room} to copy values from.
     */
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

    /**
     * Creates a new instance of {@link GuestModel} with default values.
     */
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
            String amenitiesString = amenitiesMap.keySet().stream()
                    .map(Enum::name)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            log.debug("Amenities changed, new value: {}", amenitiesString);
            amenities.setValue(amenitiesString);
        });

        id = new SimpleStringProperty("");
    }

    /**
     * Returns the number of beds.
     * @return The number of beds as {@link Integer}.
     */
    public Integer getNumberOfBeds() {
        return numberOfBeds.get();
    }

    /**
     * Returns the number of beds property.
     * @return The property.
     */
    public ObjectProperty<Integer> numberOfBedsProperty() {
        return numberOfBeds;
    }

    /**
     * Returns the price per night.
     * @return Price per night.
     */
    public String getPricePerNight() {
        return pricePerNight.get();
    }

    /**
     * The price per night property.
     * @return the property.
     */
    public SimpleStringProperty pricePerNightProperty() {
        return pricePerNight;
    }

    /**
     * Returns the size of the room in square meters.
     * @return Size in square meters.
     */
    public String getSizeInSqrM() {
        return sizeInSqrM.get();
    }

    /**
     * The size in square meters property.
     * @return The property.
     */
    public SimpleStringProperty sizeInSqrMProperty() {
        return sizeInSqrM;
    }

    /**
     * Returns the distance from the city center.
     * @return Distance from city center.
     */
    public String getDistanceFromCityCenter() {
        return distanceFromCityCenter.get();
    }

    /**
     * The distance from the city center property.
     * @return The property.
     */
    public SimpleStringProperty distanceFromCityCenterProperty() {
        return distanceFromCityCenter;
    }

    /**
     * Returns the distance from the beach.
     * @return Distance from beach.
     */
    public String getDistanceFromBeach() {
        return distanceFromBeach.get();
    }

    /**
     * The distance from the beach property.
     * @return The property.
     */
    public SimpleStringProperty distanceFromBeachProperty() {
        return distanceFromBeach;
    }

    /**
     * Returns the room number.
     * @return The room number.
     */
    public String getRoomNumber() {
        return roomNumber.get();
    }

    /**
     * The room number property.
     * @return The property.
     */
    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }

    /**
     * Returns the UUID of the room.
     * @return The unique ID as a {@link UUID}.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the string representation of the room ID.
     * @return The ID string.
     */
    public String getId() {
        return id.get();
    }

    /**
     * The room ID property.
     * @return The property.
     */
    public SimpleStringProperty idProperty() {
        return id;
    }

    /**
     * Returns the boolean property tracking a specific amenity status.
     * @param amenity The amenity to check.
     * @return The property representing if the amenity is selected.
     */
    public SimpleBooleanProperty getBoolPropertyForAmenity(Room.Amenity amenity) {
        return amenitiesMap.get(amenity);
    }

    /**
     * The comma-separated amenities string property.
     * @return The property.
     */
    public SimpleStringProperty amenitiesProperty() {
        return amenities;
    }

    /**
     * Converts this presentation model back into a core {@link Room} domain entity.
     * @return A newly built {@link Room} object.
     */
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