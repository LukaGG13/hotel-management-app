package io.github.lukagg13.hotelmanagementapp.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * A {@link Guest} {@link Record}.
 * @param uuid The {@link UUID} of the {@link Guest}.
 * @param name The name of the {@link Guest} as {@link String}.
 * @param age The age of the {@link Guest} as {@link Integer}.
 */
public record Guest(UUID uuid, String name, Integer age) implements Serializable {
}
