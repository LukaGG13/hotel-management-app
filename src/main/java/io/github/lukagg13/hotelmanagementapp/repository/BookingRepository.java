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

public class BookingRepository implements Repository<Booking> {

    private final Connection connection;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    static final Logger log = LoggerFactory.getLogger(BookingRepository.class);

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
        } catch (Exception e) {
            log.error("Error while getting all bookings: {}", e.getMessage());
            throw new DatabaseException(e);
        }
    }

    @Override
    public Optional<Booking> getWithUUID(UUID uuid) {
        //TODO:
        return getAll().stream().filter(booking -> booking.uuid().equals(uuid)).findFirst();
    }

    @Override
    public boolean deleteWithUUID(UUID uuid) {
        final var query = "DELETE FROM bookings WHERE id=?";
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.setString(1, uuid.toString());
            return prepareStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            log.error("Error while deleting booking with uuid {}: {}", uuid, e.getMessage());
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean update(Booking elem) {
        return deleteWithUUID(elem.uuid()) && create(elem);
    }

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

    private Set<Guest> getGuestForBooking(ResultSet resultSet) throws SQLException {
        var guestSet = new HashSet<Guest>();
        var bookingUUID = resultSet.getObject("id", UUID.class);
        final var query = "SELECT guest_id FROM guests_in_booking WHERE booking_id=?";
        try( var ptst = connection.prepareStatement(query)) {
            ptst.setString(1, bookingUUID.toString());
            try(var rs = ptst.executeQuery()) {
                while(rs.next()) {
                    var guestUUID = rs.getObject("guest_id", UUID.class);
                    guestRepository.getWithUUID(guestUUID).ifPresent(guestSet::add);
                }
            }
        } catch (SQLException e) {
            log.error("Error while getting guests for booking: {}", e.getMessage());
            throw new DatabaseException(e);
        }

        return guestSet;
    }

    private Booking resultSetToBooking(ResultSet resultSet) throws SQLException {

        var bookingUUID = resultSet.getObject("id", UUID.class);
        var roomUUID = resultSet.getObject("room_id", UUID.class);
        var checkIn = resultSet.getDate("check_in").toLocalDate();
        var checkOut= resultSet.getDate("check_out").toLocalDate();

        return new Booking(bookingUUID, getGuestForBooking(resultSet), roomRepository.getWithUUID(roomUUID).orElseThrow() , checkIn, checkOut);
    }
}