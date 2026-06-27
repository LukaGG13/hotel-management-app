package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public boolean create(Room room) {
        return roomRepository.create(room);
    }

    public List<Room> getAll() {
        return roomRepository.getAll();
    }

    public Optional<Room> getWithUUID(UUID uuid) {
        return roomRepository.getWithUUID(uuid);
    }

    public Boolean deleteWithUUID(UUID uuid) {
        return roomRepository.deleteWithUUID(uuid);
    }

    public Boolean update(Room room) {
        return roomRepository.update(room);
    }
}

