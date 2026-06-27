package io.github.lukagg13.hotelmanagementapp.entity;

import java.io.Serializable;
import java.util.UUID;

public record Guest(UUID uuid, String name, Integer age) implements Serializable {
}
