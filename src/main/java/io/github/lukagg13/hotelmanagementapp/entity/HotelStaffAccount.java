package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

/**
 * A class representing an {@link HotelStaffAccount} {@link User}.
 */
public class HotelStaffAccount extends User{

    /**
     * Returns an {@link HotelStaffAccount} object.
     * @param uuid The {@link UUID} of the {@link HotelStaffAccount}.
     * @param userName The username of the {@link HotelStaffAccount} as a {@link String}.
     * @param role The role of the {@link HotelStaffAccount} as {@link Integer}
     */
    public HotelStaffAccount(UUID uuid, String userName, Integer role) {
        super(uuid, userName, role);
    }
}
