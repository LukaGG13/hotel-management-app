package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.exception.IncorrectPasswordException;
import io.github.lukagg13.hotelmanagementapp.exception.IncorrectUsernameException;
import io.github.lukagg13.hotelmanagementapp.file.PasswordUtils;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.entity.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A service used to manage login's and active users in the app.
 */
public final class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private static final UsersRepository repository = new UsersRepository(DatabaseUtils.createConnection());
    private static User loggedInUser = null;

    /**
     * Private constructor to hide the implicit one.
     */
    private LoginService() {}

    /**
     * A method used to log in a user in to the app.
     * @param name The name of the user.
     * @param password The password of the user.
     * @throws IncorrectUsernameException If there is no user with such username.
     * @throws IncorrectPasswordException If the password hash's don't mach.
     */
    public static void login(String name, String password) throws IncorrectUsernameException, IncorrectPasswordException {
        log.debug("Login attempt in in as: {} with password: {}", name, password);

        var resultList = repository.getAll().stream().filter(user -> user.getUserName().equals(name)).toList();
        if (resultList.isEmpty()) throw new IncorrectUsernameException("No user found");

        var user = resultList.getFirst();

        if(Boolean.FALSE.equals(PasswordUtils.check(user.getUuid(), password))) throw new IncorrectPasswordException("Password doesn't match");

        log.debug("Login in successful login in as user: {}", user);
        loggedInUser = user;
    }

    /**
     * User to log out the users.
     */
    public static void logout() {
        loggedInUser = null;
    }

    /**
     * Returns the option of the {@link User}.
     * @return A {@link Optional} of the logged in {@link User}.
     */
    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(loggedInUser);
    }

    /**
     * Gets the username of the logged in {@link User}.
     * @return The username of the logged in {@link User} or "Unknown User" as string if no
     * one is logged in.
     */
    public static String getLoggedInUsername() {
        return  loggedInUser != null ? loggedInUser.getUserName() : "Unknown User";
    }
}
