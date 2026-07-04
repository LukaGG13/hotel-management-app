package io.github.lukagg13.hotelmanagementapp.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * A {@link Booking} {@link Record}.
 * @param uuid The {@link UUID} of the {@link Booking}.
 * @param guests A {@link Set} of the {@link Guest}'s that that will stay in the booked {@link Room}.
 * @param room A {@link Room} that was booked.
 * @param checkInDate A {@link LocalDate} of the checkin.
 * @param checkOutDate A {@link LocalDate} of the checkout.
 */
public record Booking(UUID uuid, Set<Guest> guests, Room room, LocalDate checkInDate, LocalDate checkOutDate) implements Serializable {
    @Override
    public String toString() {
        return "Booking{\n" +
                "uuid=" + uuid +
                ",\n guests=" + guests.toString().replace("\n", "\n\t") +
                ",\n room=" + room.toString().replace("\n", "\n\t") +
                ",\n checkInDate=" + checkInDate +
                ",\n checkOutDate=" + checkOutDate +
                '}';
    }
}
