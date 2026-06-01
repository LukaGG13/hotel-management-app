package io.github.lukagg13.hotelmanagementapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<E> {
    List<E> getAll();
    Optional<E> getWithUUID(UUID uuid);
    boolean deleteWithUUID(UUID uuid);
    boolean update(E elem);
    boolean create(E elem);
}
