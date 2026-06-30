package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

/**
 * A class representing an {@link Admin} {@link User}.
 */
public class Admin extends User {

    /**
     * Returns an {@link Admin} object.
     * @param uuid The {@link UUID} of the {@link Admin}.
     * @param userName The username of the {@link Admin} as a {@link String}.
     * @param role The role of the {@link Admin} as {@link Integer}
     */
    public Admin(UUID uuid, String userName, Integer role) {
        //TODO: change role to and enum.
        super(uuid, userName, role);
    }
}
