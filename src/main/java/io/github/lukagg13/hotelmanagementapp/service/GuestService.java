package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class GuestService {
    private static final Logger log = LoggerFactory.getLogger(GuestService.class);
    private final GuestRepository guestRepository;
    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public boolean create(Guest guest) {
        //History.addLog();
        return guestRepository.create(guest);
    }

    public List<Guest> getAll() {
        return guestRepository.getAll();
    }

    public Optional<Guest> getWithUUID(UUID uuid) {
       return guestRepository.getWithUUID(uuid);
    }

    public Boolean deleteWithUUID(UUID uuid) {
        return guestRepository.deleteWithUUID(uuid);
    }

    public Boolean update(Guest guest) {
        return guestRepository.update(guest);
    }

}
