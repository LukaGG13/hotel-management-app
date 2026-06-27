package io.github.lukagg13.hotelmanagementapp.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Class representing a room
 * @version 1.0
 * @author luka
 */
public final class Room {
    private static final Logger log = LoggerFactory.getLogger(Room.class);
    private final UUID uuid;
    private final Integer numOfBeds;
    private final Integer sizeInSqrM;
    private final BigDecimal pricePerNight;
    private final BigDecimal distanceFromCityCenter;
    private final BigDecimal distanceFromBeach;
    private final Integer roomNumber;
    public enum Amenity {GYM, WIFI, POOL, PARKING, SPA, BREAKFAST;}
    private final Set<Amenity> amenities;

    /**
     * RoomBuilder - using builder pattern.
     * @version 1.0
     * @author luka
     */
    public static final class RoomBuilder {
        private final UUID uuid;
        private final Integer numOfBeds;
        private final BigDecimal pricePerNight;
        private Integer sizeInSqrM = 0;
        private BigDecimal distanceFromCityCenter =  new BigDecimal(0);
        private BigDecimal distanceFromBeach = new BigDecimal(0);
        private final EnumSet<Amenity> amenities = EnumSet.noneOf(Amenity.class);
        private Integer roomNumber = 101;

        /**
         * Constructor for RoomBuilder.
         * @param uuid The uuid of the room, as {@link UUID}.
         * @param numOfBeds Number of beds in the room, as {@link Integer}.
         * @param pricePerNight Price for one night in euros, as {@link BigDecimal}.
         */
        public RoomBuilder(UUID uuid, Integer numOfBeds, BigDecimal pricePerNight) {
            log.info("Created a room builder with number of beds:{} and price per night:{}",numOfBeds, pricePerNight);
            this.uuid = uuid;
            this.numOfBeds = numOfBeds;
            this.pricePerNight = pricePerNight;
        }

        /**
         * Constructor for RoomBuilder.
         * @param numOfBeds Number of beds in the room, as {@link Integer}.
         * @param pricePerNight Price for one night in euros, as {@link BigDecimal}.
         */
        public RoomBuilder(Integer numOfBeds, BigDecimal pricePerNight) {
            log.info("Created a room builder with number of beds:{} and price per night:{}",numOfBeds, pricePerNight);
            this.uuid = UUID.randomUUID();
            this.numOfBeds = numOfBeds;
            this.pricePerNight = pricePerNight;
        }

        /**
         * Add a {@link Amenity} for the room.
         * @param amenity {@link Amenity} to be added.
         * @return {@link RoomBuilder} for builder pattern.
         */
        public RoomBuilder addAmenity(Amenity amenity) {
            amenities.add(amenity);
            return this;
        }
        /**
         * Sets the size of the room in square meters.
         * @param sizeInSqrM the size of the room to be set in meters, as {@link Integer}.
         * @return The current {@link RoomBuilder} for builder pattern.
         */
        public RoomBuilder sizeInSqrM(Integer sizeInSqrM) {
            this.sizeInSqrM = sizeInSqrM;
            return this;
        }

        /**
         * Sets the distance from City Center in kilometers.
         * @param distanceFromCityCenter distance from city center to be set in kilometers, as {@link BigDecimal}.
         * @return The current {@link RoomBuilder} for builder pattern.
         */
        public RoomBuilder distanceFromCityCenter(BigDecimal distanceFromCityCenter) {
            this.distanceFromCityCenter = distanceFromCityCenter;
            return this;
        }

        /**
         * Sets the distance from Beach in kilometers.
         * @param distanceFromBeach distance from beach to be set in kilometers, as {@link BigDecimal}.
         * @return The current {@link RoomBuilder} for builder pattern.
         */
        public RoomBuilder distanceFromBeach(BigDecimal distanceFromBeach) {
            this.distanceFromBeach = distanceFromBeach;
            return this;
        }

        /**
         * Sets the room number.
         * @param roomNumber as {@link Integer}.
         * @return The current {@link RoomBuilder} for builder pattern.
         */
        public RoomBuilder roomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        /**
         * Build method for builder pattern.
         * @return A new {@link Room} object.
         */
        public Room build() {
            return new Room(this);
        }
    }

    /**
     * Room constructor using builder.
     * @param roomBuilder {@link RoomBuilder} object to be built from.
     */
    Room(RoomBuilder roomBuilder) {
        log.info("created new room");
        this.uuid = roomBuilder.uuid;
        this.numOfBeds = roomBuilder.numOfBeds;
        this.sizeInSqrM = roomBuilder.sizeInSqrM;
        this.pricePerNight = roomBuilder.pricePerNight;
        this.distanceFromCityCenter = roomBuilder.distanceFromCityCenter;
        this.distanceFromBeach = roomBuilder.distanceFromBeach;
        this.amenities = roomBuilder.amenities;
        this.roomNumber = roomBuilder.roomNumber;
    }

    /**
     * Gets the number of beds in the room.
     * @return number of beds, as {@link Integer}.
     */
    public Integer getNumOfBeds() {
        return numOfBeds;
    }

    /**
     * Gets the size of the room in square meters.
     * @return size of the room, as {@link Integer}.
     */
    public Integer getSizeInSqrM() {
        return sizeInSqrM;
    }

    /**
     * Returns the price for one night stay in euros.
     * @return the price of stay for one night, as {@link BigDecimal}.
     */
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    /**
     * Gets the distance of the room form city center in kilometers.
     * @return The distance from city center in kilometers, as {@link BigDecimal}
     */
    public BigDecimal getDistanceFromCityCenter() {
        return distanceFromCityCenter;
    }

    /**
     * Gets the distance of the room form the beach in kilometers.
     * @return The distance from the beach in kilometers, as {@link BigDecimal}
     */
    public BigDecimal getDistanceFromBeach() {
        return distanceFromBeach;
    }

    /**
     * Get the id of the {@link Room} as {@link UUID}.
     * @return the id of the {@link Room} as {@link UUID}.
     */
    public UUID getId() {
        return uuid;
    }

    /**
     * Gets the number of the {@link Room} as{@link Integer}.
     * @return the number of the {@link Room} as {@link Integer}.
     */
    public Integer getRoomNumber() {
        return roomNumber;
    }

    /**
     * Gets the amenities of the room as {@link EnumSet} of {@link Amenity}'s.
     * @return A {@link EnumSet} of {@link Amenity}'s.
     */
    public Set<Amenity> getAmenities() {
        return amenities;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Room room)) return false;
        return Objects.equals(uuid, room.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "Room{" +
                "numOfBeds=" + numOfBeds +
                ", sizeInSqrM=" + sizeInSqrM +
                ", pricePerNight=" + pricePerNight +
                ", distanceFromCityCenter=" + distanceFromCityCenter +
                ", distanceFromBeach=" + distanceFromBeach +
                ", amenities=" + amenities +
                '}';
    }
}