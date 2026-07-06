package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.entity.Admin;
import io.github.lukagg13.hotelmanagementapp.entity.HotelStaffAccount;
import io.github.lukagg13.hotelmanagementapp.entity.User;
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
 * Repository used to interact with the {@link User}'s from the Database.
 */
public final class UsersRepository implements Repository<User>  {

    final Connection connection;
    static final Logger log = LoggerFactory.getLogger(UsersRepository.class);

    /**
     * Returns a new {@link UsersRepository} instance.
     * @param connection The {@link Connection} that will be used to interact with the database.
     */
    public UsersRepository(Connection connection) {
        this.connection = connection;
        log.info("Creating UsersRepository instance.");
    }

    @Override
    public List<User> getAll() {
        log.info("Getting all users.");
        final String query = "SELECT id, username, role_id FROM users;";
        var userList = new ArrayList<User>();
        try (   var prepareStatement = connection.prepareStatement(query);
                var resultSet = prepareStatement.executeQuery()) {
            while (resultSet.next()) {
                userList.add(resultSetToUser(resultSet));
            }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return userList;
    }

    @Override
    public Optional<User> getWithUUID(UUID uuid) {
        log.info("Getting user with uuid: {}.", uuid);
        final String query = "SELECT id, username, role_id FROM users WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

           try(var resultSet = prepareStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToUser(resultSet));
                }
           }
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteWithUUID(UUID uuid) {
        log.info("Deleting user with uuid: {}.", uuid);
        final String query = "DELETE FROM users WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(User elem) {
        log.info("Updating user with uuid: {}.", elem.getUuid());
        final String query = "UPDATE users SET username = ?, role_id = ? WHERE id = ?;";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.getUserName());
            prepareStatement.setInt(2, elem.getRole());
            prepareStatement.setString(3, elem.getUuid().toString());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean create(User elem) {
        final String query = "INSERT INTO users (id, username, role_id) VALUES (?, ?, ?);";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.getUuid().toString());
            prepareStatement.setString(2, elem.getUserName());
            prepareStatement.setInt(3, elem.getRole());

            return prepareStatement.executeUpdate() == 1;
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns a {@link User} from a {@link ResultSet}.
     * @param resultSet The {@link ResultSet} from which to get the {@link User}.
     * @return The created {@link User}.
     * @throws SQLException If there is an error with the database.
     */
    private User resultSetToUser(ResultSet resultSet) throws SQLException {
        var uuid = resultSet.getObject("id", UUID.class);
        var username = resultSet.getString("username");
        var roleId = resultSet.getInt("role_id");

        if(roleId == 0) {
            return new Admin(uuid, username, roleId);
        } else {
            return new HotelStaffAccount(uuid, username, roleId);
        }
    }
}