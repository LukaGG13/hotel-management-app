package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import io.github.lukagg13.hotelmanagementapp.repository.BookingRepository;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

public class BookingService {
    private final BookingRepository bookingRepository;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository) {
        logger.info("Creating Booking Service instance.");
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAll() {
        logger.info("Getting all bookings");
        return bookingRepository.getAll();
    }

    public boolean create(Booking booking) {
        logger.info("Creating booking: {}", booking);
        return bookingRepository.create(booking);
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean deleteWithUUID(UUID uuid) {
        logger.info("Deleting booking with uuid: {}", uuid);
        return bookingRepository.deleteWithUUID(uuid);
    }

    public boolean update(Booking booking) {
        logger.info("Updating booking {}", booking);
        return bookingRepository.update(booking);
    }
}
