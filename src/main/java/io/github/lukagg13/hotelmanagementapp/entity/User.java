package io.github.lukagg13.hotelmanagementapp.entity;

import java.util.UUID;

/**
 * An abstract class representing a {@link User} of the application.
 */
public abstract class User {
    private UUID uuid;
    private String userName;
    private Integer role;

    /**
     * A constructor for the abstract {@link User} class.
     * @param uuid The {@link UUID} of the {@link User}.
     * @param userName The username as a {@link String}.
     * @param role TODO: enum
     */
    protected User(UUID uuid, String userName, Integer role) {
        this.uuid = uuid;
        this.userName = userName;
        this.role = role;
    }

    /**
     * Get the {@link UUID}.
     * @return {@link UUID}.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sets the {@link UUID}.
     * @param uuid The {@link UUID} to be set.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the username.
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username
     * @param userName The username
     */
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
