package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

public abstract class User {
    private UUID uuid;
    private String userName;
    private Integer role;

    protected User(UUID uuid, String userName, Integer role) {
        this.uuid = uuid;
        this.userName = userName;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
