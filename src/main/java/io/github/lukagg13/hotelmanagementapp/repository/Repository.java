package io.github.lukagg13.hotelmanagementapp.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * An interface for repositories.
 * @param <E> The type of the Repository.
 */
public sealed interface Repository<E> permits GuestRepository, BookingRepository, UsersRepository, RoomRepository {
    /**
     * Returns a list of all the elements.
     * @return A {@link List} of all the elements.
     */
    List<E> getAll();

    /**
     * Gets the element with the matching {@link UUID}.
     * @param uuid The {@link UUID} of the element.
     * @return The element
     */
    Optional<E> getWithUUID(UUID uuid);

    /**
     * Deletes the element with the matching {@link UUID}.
     * @param uuid The {@link UUID} of the element.
     * @return True if successful else false.
     */
    boolean deleteWithUUID(UUID uuid);

    /**
     * Updates the element.
     * @param elem The element to be updated.
     * @return True if successful else false.
     */
    boolean update(E elem);

    /**
     * Creates an element.
     * @param elem The element to be created.
     * @return True if successful else false.
     */
    boolean create(E elem);
}
