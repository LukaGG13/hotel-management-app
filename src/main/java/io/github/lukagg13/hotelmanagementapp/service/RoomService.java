package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The service which will be used to manage {@link Room}'s in the app.
 */
public final class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    /**
     * Creates a new {@link RoomService} object.
     * @param roomRepository The repository which will be used to manage the database operations.
     */
    public RoomService(RoomRepository roomRepository) {
        log.info("Creating a new room service");
        this.roomRepository = roomRepository;
    }

    /**
     * Saves a new {@link Room}.
     * @param room The {@link Room} which will be saved.
     * @return True if successful else false.
     */
    public boolean create(Room room) {
        log.info("Creating new room {}", room);
        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), null, room));

        return roomRepository.create(room);
    }

    /**
     * Returns a {@link List} of all the {@link Room}'s.
     * @return {@link List} of all {@link Room}'s.
     */
    public List<Room> getAll() {
        log.info("Getting all rooms");
        return roomRepository.getAll();
    }

    /**
     * Returns an Optional of a {@link Room} with a specific {@link UUID}.
     * @param uuid The {@link UUID} of the {@link Room} we want to return.
     * @return The optional of the {@link Room} with the corresponding {@link UUID}.
     */
    public Optional<Room> getWithUUID(UUID uuid) {
        log.info("Getting room with uuid: {}", uuid);
        return roomRepository.getWithUUID(uuid);
    }

    /**
     * Deletes the {@link Room} with the {@link UUID}.
     * @param uuid The {@link UUID} of the {@link Room} to be deleted.
     * @return true if successful else false.
     */
    public Boolean deleteWithUUID(UUID uuid) {
        var oldValueOptional = getWithUUID(uuid);
        var oldValue = oldValueOptional.orElse(null);
        log.info("Deleting room: {}", oldValue);

        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), oldValue, null));

        return roomRepository.deleteWithUUID(uuid);
    }
    /**
     * Updates a {@link Room}.
     * @param room The {@link Room} which will be updated.
     * @return true if successful else false.
     */
    public Boolean update(Room room) {
        var oldValueOptional = getWithUUID(room.getId());
        var oldValue = oldValueOptional.orElse(null);
        log.info("Updating guest: {}", oldValue);
        History.addLog(new HistoryRecordLog(LoginService.getLoggedInUsername(), LocalDateTime.now(), oldValue, room));

        return roomRepository.update(room);
    }
}

