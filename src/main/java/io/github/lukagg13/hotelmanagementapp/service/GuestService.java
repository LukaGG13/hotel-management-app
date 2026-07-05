package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A service used to manage guests in the app.
 */
public final class GuestService {
    private static final Logger log = LoggerFactory.getLogger(GuestService.class);
    private final GuestRepository guestRepository;

    /**
     * Creates a new {@link GuestService} object.
     * @param guestRepository The repository which will be used to manage the database operations.
     */
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    /**
     * Create a new {@link Guest} and saves it to the {@link GuestRepository}.
     * @param guest The {@link Guest} to be saved.
     * @return true if successful else false.
     */
    public boolean create(Guest guest) {
        log.info("Creating guest: {}", guest);
        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), null, guest));
        return guestRepository.create(guest);
    }

    /**
     * Gets all the guests.
     * @return A {@link List} of all the {@link Guest}'s.
     */
    public List<Guest> getAll() {
        log.info("Getting all guests");
        return guestRepository.getAll();
    }

    /**
     * Returns an Optional of a {@link Guest} with a specific {@link UUID}.
     * @param uuid The {@link UUID} of the {@link Guest} we want to return.
     * @return The optional of the {@link Guest} with the corresponding {@link UUID}.
     */
    public Optional<Guest> getWithUUID(UUID uuid) {
       log.info("Getting guest with uuid: {}", uuid);
       return guestRepository.getWithUUID(uuid);
    }

    /**
     * Deletes the {@link Guest} with the {@link UUID}.
     * @param uuid The {@link UUID} of the {@link Guest} we want to delete.
     * @return true if successful else false.
     */
    public Boolean deleteWithUUID(UUID uuid) {
        var oldValueOptional = getWithUUID(uuid);
        var oldValue = oldValueOptional.orElse(null);
        log.info("Deleting guest: {}", oldValue);

        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), oldValue, null));

        return guestRepository.deleteWithUUID(uuid);
    }

    /**
     * Updates a {@link Guest}.
     * @param guest The {@link Guest} which will be updated.
     * @return true if successful else false.
     */
    public Boolean update(Guest guest) {
        var oldValueOptional = getWithUUID(guest.uuid());
        var oldValue = oldValueOptional.orElse(null);
        log.info("Updating guest: {}", oldValue);

        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), oldValue, guest));

        return guestRepository.update(guest);
    }

}
