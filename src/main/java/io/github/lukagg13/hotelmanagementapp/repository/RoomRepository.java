package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class RoomRepository implements Repository<Room> {

    final Connection connection;
    static final Logger log = LoggerFactory.getLogger(RoomRepository.class);

    public RoomRepository(Connection connection) {
        this.connection = connection;
        log.info("Creating RoomRepository instance.");
    }

    @Override
    public List<Room> getAll() {
        log.info("Getting all rooms.");
        final String query = "SELECT id, num_of_beds, size_in_sqr_m, price_per_night, distance_from_city_center, distance_from_beach, room_number FROM rooms;";
        var roomList = new ArrayList<Room>();
        try (var prepareStatement = connection.prepareStatement(query);
             var resultSet = prepareStatement.executeQuery()) {
            while (resultSet.next()) {
                var room = resultSetToRoom(resultSet);
                var amenities = getAmenitiesForRoom(room.getId());
                var builder = new Room.RoomBuilder(room.getId(), room.getNumOfBeds(), room.getPricePerNight())
                        .sizeInSqrM(room.getSizeInSqrM())
                        .distanceFromCityCenter(room.getDistanceFromCityCenter())
                        .distanceFromBeach(room.getDistanceFromBeach())
                        .roomNumber(room.getRoomNumber());
                for (var amenity : amenities) builder.addAmenity(amenity);
                roomList.add(builder.build());
            }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return roomList;
    }

    @Override
    public Optional<Room> getWithUUID(UUID uuid) {
        log.info("Getting room with uuid: {}.", uuid);
        final String query = "SELECT id, num_of_beds, size_in_sqr_m, price_per_night, distance_from_city_center, distance_from_beach, room_number FROM rooms WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

           try(var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    var room = resultSetToRoom(resultSet);
                    var amenities = getAmenitiesForRoom(room.getId());
                    var builder = new Room.RoomBuilder(room.getId(), room.getNumOfBeds(), room.getPricePerNight())
                            .sizeInSqrM(room.getSizeInSqrM())
                            .distanceFromCityCenter(room.getDistanceFromCityCenter())
                            .distanceFromBeach(room.getDistanceFromBeach())
                            .roomNumber(room.getRoomNumber());
                    for (var amenity : amenities) builder.addAmenity(amenity);
                    return Optional.of(builder.build());
                }
           }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteWithUUID(UUID uuid) {
        log.info("Deleting room with uuid: {}.", uuid);
        final String query = "DELETE FROM rooms WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(Room elem) {
        log.info("Updating room with uuid: {}.", elem.getId());
        final String query = "UPDATE rooms SET num_of_beds = ?, size_in_sqr_m = ?, price_per_night = ?, distance_from_city_center = ?, distance_from_beach = ?, room_number = ? WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setInt(1, elem.getNumOfBeds());
            prepareStatement.setInt(2, elem.getSizeInSqrM());
            prepareStatement.setBigDecimal(3, elem.getPricePerNight());
            prepareStatement.setBigDecimal(4, elem.getDistanceFromCityCenter());
            prepareStatement.setBigDecimal(5, elem.getDistanceFromBeach());
            prepareStatement.setInt(6, elem.getRoomNumber());
            prepareStatement.setString(7, elem.getId().toString());

            var updated = prepareStatement.executeUpdate() == 1;
            try (var deletePrepareStatement = connection.prepareStatement("DELETE FROM room_amenities WHERE room_id = ?")) {
                deletePrepareStatement.setString(1, elem.getId().toString());
                deletePrepareStatement.executeUpdate();
            }
            insertAmenitiesForRoom(elem);

            return updated;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean create(Room elem) {
        log.info("Creating room: {}", elem);
        final String query = "INSERT INTO rooms (id, num_of_beds, size_in_sqr_m, price_per_night, distance_from_city_center, distance_from_beach, room_number) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.getId().toString());
            prepareStatement.setInt(2, elem.getNumOfBeds());
            prepareStatement.setInt(3, elem.getSizeInSqrM());
            prepareStatement.setBigDecimal(4, elem.getPricePerNight());
            prepareStatement.setBigDecimal(5, elem.getDistanceFromCityCenter());
            prepareStatement.setBigDecimal(6, elem.getDistanceFromBeach());
            prepareStatement.setInt(7, elem.getRoomNumber());

            var created = prepareStatement.executeUpdate() == 1;
            insertAmenitiesForRoom(elem);
            return created;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private void insertAmenitiesForRoom(Room elem) throws SQLException {
        var amenities = elem.getAmenities();
        if (amenities == null || amenities.isEmpty()) return;

        final var selectAmenityId = "SELECT id FROM amenities WHERE name = ?";
        final var insertRoomAmenity = "INSERT INTO room_amenities (room_id, amenity_id) VALUES (?, ?)";

        try (var selectPrepareStatement = connection.prepareStatement(selectAmenityId);
             var insertPrepareStatement = connection.prepareStatement(insertRoomAmenity)) {
            for (var a : amenities) {
                selectPrepareStatement.setString(1, a.name());
                try (var rs = selectPrepareStatement.executeQuery()) {
                    if (rs.next()) {
                        var amenityId = rs.getInt("id");
                        insertPrepareStatement.setString(1, elem.getId().toString());
                        insertPrepareStatement.setInt(2, amenityId);
                        insertPrepareStatement.addBatch();
                    }
                }
            }
            insertPrepareStatement.executeBatch();
        }
    }

    private Set<Room.Amenity> getAmenitiesForRoom(UUID roomId) throws SQLException {
        final var query = "SELECT a.name FROM amenities a JOIN room_amenities ra ON a.id = ra.amenity_id WHERE ra.room_id = ?";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, roomId.toString());
            try (var resultSet = preparedStatement.executeQuery()) {
                var set = EnumSet.noneOf(Room.Amenity.class);
                while (resultSet.next()) {
                    var name = resultSet.getString("name");
                    set.add(Room.Amenity.valueOf(name));
                }
                return set;
            }
        }
    }

    private Room resultSetToRoom(ResultSet resultSet) throws SQLException {
        var uuid = resultSet.getObject("id", UUID.class);
        var numOfBeds = resultSet.getInt("num_of_beds");
        var size = resultSet.getInt("size_in_sqr_m");
        var price = resultSet.getBigDecimal("price_per_night");
        var distCity = resultSet.getBigDecimal("distance_from_city_center");
        var distBeach = resultSet.getBigDecimal("distance_from_beach");
        var roomNumber = resultSet.getInt("room_number");

        var builder = new Room.RoomBuilder(uuid, numOfBeds, price)
                .sizeInSqrM(size)
                .distanceFromCityCenter(distCity == null ? BigDecimal.ZERO : distCity)
                .distanceFromBeach(distBeach == null ? BigDecimal.ZERO : distBeach)
                .roomNumber(roomNumber);


        return builder.build();
    }
}

