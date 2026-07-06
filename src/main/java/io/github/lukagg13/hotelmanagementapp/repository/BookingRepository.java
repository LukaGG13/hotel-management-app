package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Repository used to interact with {@link Booking}'s from the Database.
 */
public final class BookingRepository implements Repository<Booking> {

    private final Connection connection;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    static final Logger log = LoggerFactory.getLogger(BookingRepository.class);

    /**
     * Returns a new {@link BookingRepository} instance.
     * @param connection The {@link Connection} that will be used to interact with the database.
     */
    public BookingRepository(Connection connection) {
        this.connection = connection;
        log.info("Creating BookingRepository instance.");
        roomRepository = new RoomRepository(connection);
        guestRepository = new GuestRepository(connection);
    }

    @Override
    public List<Booking> getAll() {
        final var query = "SELECT id, room_id, check_in, check_out FROM bookings";
        try (var prepareStatement = connection.prepareStatement(query);
             var resultSet = prepareStatement.executeQuery()) {
            var bookingList = new java.util.ArrayList<Booking>();
            while (resultSet.next()) {
                bookingList.add(resultSetToBooking(resultSet));
            }
            return bookingList;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<Booking> getWithUUID(UUID uuid) {
        return getAll().stream().filter(booking -> booking.uuid().equals(uuid)).findFirst();
    }

    @Override
    public boolean deleteWithUUID(UUID uuid) {
        final var query = "DELETE FROM bookings WHERE id=?";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());
            return prepareStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(Booking elem) {
        return deleteWithUUID(elem.uuid()) && create(elem);
    }

    /**
     * Add a {@link Guest} to the guests_in_booking table.
     * @param elem The {@link Guest} to be added.
     * @return True if successful else false
     * @throws SQLException If there is an error with the database.
     */
    private boolean addGuestsToBooking(Booking elem) throws SQLException {
       final var query = "INSERT INTO guests_in_booking(booking_id, guest_id) VALUES(?,?)";
       for(var guest : elem.guests()) {
           try(var prepareStatement = connection.prepareStatement(query)) {
               prepareStatement.setString(1, elem.uuid().toString());
               prepareStatement.setString(2, guest.uuid().toString());
               if(prepareStatement.executeUpdate() != 1) {
                   return false;
               }
           }
       }
       return true;
    }
    @Override
    public boolean create(Booking elem)  {
        final var query = "INSERT INTO bookings(id, room_id, check_in, check_out) VALUES(?, ?, ?, ?)";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, elem.uuid().toString());
            prepareStatement.setString(2, elem.room().getId().toString());
            prepareStatement.setDate(3, java.sql.Date.valueOf(elem.checkInDate()));
            prepareStatement.setDate(4, java.sql.Date.valueOf(elem.checkOutDate()));

            if(prepareStatement.executeUpdate() != 1) throw new DatabaseException("Failed to save booking!");
            return addGuestsToBooking(elem);
        } catch (SQLException e) {
            throw  new DatabaseException(e);
        }
    }

    /**
     * Returns the {@link Guest} in the {@link Booking}.
     * @param resultSet The {@link ResultSet} of the query to get the booking UUID.
     * @return A {@link Set} of {@link Guest} that are in the {@link Booking} from the resultSet.
     * @throws SQLException If there is an error with the Database.
     */
    private Set<Guest> getGuestForBooking(ResultSet resultSet) throws SQLException {
        var guestSet = new HashSet<Guest>();
        var bookingUUID = resultSet.getObject("id", UUID.class);
        final var query = "SELECT guest_id FROM guests_in_booking WHERE booking_id=?";
        try( var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bookingUUID.toString());
            try(var rs = preparedStatement.executeQuery()) {
                while(rs.next()) {
                    var guestUUID = rs.getObject("guest_id", UUID.class);
                    guestRepository.getWithUUID(guestUUID).ifPresent(guestSet::add);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return guestSet;
    }

    /**
     * Returns a {@link Booking} from a {@link ResultSet}.
     * @param resultSet The {@link ResultSet} from which to get the {@link Booking}.
     * @return The created {@link Booking}.
     * @throws SQLException If there is an error with the database.
     */
    private Booking resultSetToBooking(ResultSet resultSet) throws SQLException {

        var bookingUUID = resultSet.getObject("id", UUID.class);
        var roomUUID = resultSet.getObject("room_id", UUID.class);
        var checkIn = resultSet.getDate("check_in").toLocalDate();
        var checkOut= resultSet.getDate("check_out").toLocalDate();

        return new Booking(bookingUUID, getGuestForBooking(resultSet), roomRepository.getWithUUID(roomUUID).orElseThrow() , checkIn, checkOut);
    }
}