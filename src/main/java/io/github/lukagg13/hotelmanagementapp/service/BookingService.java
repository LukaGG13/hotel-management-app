package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import io.github.lukagg13.hotelmanagementapp.repository.BookingRepository;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service used to manage {@link Booking}'s in the app.
 */
public final class BookingService {
    private final BookingRepository bookingRepository;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BookingService.class);

    /**
     * Creates a new {@link BookingService} object.
     * @param bookingRepository The repository which will be used to manage the database operations.
     */
    public BookingService(BookingRepository bookingRepository) {
        logger.info("Creating Booking Service instance.");
        this.bookingRepository = bookingRepository;
    }

    /**
     * Gets all the {@link Booking}'s
     * @return A {@link List} of all {@link Booking}'s.
     */
    public List<Booking> getAll() {
        logger.info("Getting all bookings");
        return bookingRepository.getAll();
    }

    /**
     * Creates a new {@link Booking}.
     * @param booking The created {@link Booking}.
     * @return True if successful else false.
     */
    public boolean create(Booking booking) {
        logger.info("Creating booking: {}", booking);
        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), null, booking));

        return bookingRepository.create(booking);
    }

    /**
     * Deletes the {@link Booking} with the matching {@link UUID}.
     * @param uuid The {@link UUID} of the {@link Booking} to be deleted.
     * @return True if successful else false.
     */
    public boolean deleteWithUUID(UUID uuid) {
        logger.info("Deleting booking with uuid: {}", uuid);
        var old = getAll().stream().filter(b -> b.uuid().equals(uuid)).findFirst().orElse(null);

        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), old, null));

        return bookingRepository.deleteWithUUID(uuid);
    }


    /**
     * Updates a {@link Booking}.
     * @param booking The {@link Booking} to be updated.
     * @return True if successful else false.
     */
    public boolean update(Booking booking) {
        logger.info("Updating booking {}", booking);
        var old = getAll().stream().filter(b -> b.uuid().equals(booking.uuid())).findFirst().orElse(null);
        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), old, booking));

        return bookingRepository.update(booking);
    }
}
