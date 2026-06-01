package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

public class Admin extends User {
    public Admin(UUID uuid, String userName, Integer role) {
        super(uuid, userName, role);
    }
}
