package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

public class HotelStaffAccount extends User{
    public HotelStaffAccount(UUID uuid, String userName, Integer role) {
        super(uuid, userName, role);
    }
}
