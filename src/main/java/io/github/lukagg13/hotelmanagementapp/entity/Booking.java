package io.github.lukagg13.hotelmanagementapp.entity;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record Booking(UUID uuid, Set<Guest> guests, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
}
