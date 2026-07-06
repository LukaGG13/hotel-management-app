package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository used to interact with {@link Guest}'s from the Database.
 */
public final class GuestRepository implements Repository<Guest> {

    final Connection connection;
    static final Logger log = LoggerFactory.getLogger(GuestRepository.class);

    /**
     * Returns a new {@link GuestRepository} instance.
     * @param connection The {@link Connection} that will be used to interact with the database.
     */
    public GuestRepository(Connection connection) {
        this.connection = connection;
        log.info("Creating UsersRepository instance.");
    }

    @Override
    public List<Guest> getAll() {
        log.info("Getting all guests.");
        final String query = "SELECT id, name, age FROM guests;";
        var guestList = new ArrayList<Guest>();
        try (   var prepareStatement = connection.prepareStatement(query);
                var resultSet = prepareStatement.executeQuery()) {
            while (resultSet.next()) {
                guestList.add(resultSetToGuest(resultSet));
            }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return guestList;
    }

    @Override
    public Optional<Guest> getWithUUID(UUID uuid) {
        log.info("Getting guest with uuid: {}.", uuid);
        final String query = "SELECT id, name, age FROM guests WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

           try(var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToGuest(resultSet));
                }
           }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteWithUUID(UUID uuid) {
        log.info("Deleting guest with uuid: {}.", uuid);
        final String query = "DELETE FROM guests WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(Guest elem) {
        log.info("Updating guest with uuid: {}.", elem.uuid());
        final String query = "UPDATE guests SET name = ?, age = ? WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.name());
            prepareStatement.setInt(2, elem.age());
            prepareStatement.setString(3, elem.uuid().toString());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean create(Guest elem) {
        final String query = "INSERT INTO guests (id, name, age) VALUES (?, ?, ?);";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.uuid().toString());
            prepareStatement.setString(2, elem.name());
            prepareStatement.setInt(3, elem.age());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns a {@link Guest} from a {@link ResultSet}.
     * @param resultSet The {@link ResultSet} from which to get the {@link Guest}.
     * @return The created {@link Guest}.
     * @throws SQLException If there is an error with the database.
     */
    private Guest resultSetToGuest(ResultSet resultSet) throws SQLException {
        var uuid = resultSet.getObject("id", UUID.class);
        var name = resultSet.getString("name");
        var age = resultSet.getInt("age");

        return new Guest(uuid, name, age);
    }
}
