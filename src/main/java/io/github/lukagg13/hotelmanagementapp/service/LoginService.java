package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.exception.IncorrectPasswordException;
import io.github.lukagg13.hotelmanagementapp.exception.UserNotFoundException;
import io.github.lukagg13.hotelmanagementapp.file.PasswordUtils;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.entity.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private final UsersRepository repository;
    private static User loggedInUser = null;

    public LoginService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }
    private boolean authentication(User user, String password) {
        return PasswordUtils.check(user.getUuid(), password);
    }


    public void login(String name, String password) throws UserNotFoundException, IncorrectPasswordException {
        log.debug("Login attempt in in as: {} with password: {}", name, password);

        //TODO: stavi u db zbog speed il ne igs nije bitno
        var resultList = repository.getAll().stream().filter(user -> user.getUserName().equals(name)).toList();
        if (resultList.isEmpty()) throw new UserNotFoundException("No user found");
        if (resultList.size() > 1) throw new IllegalStateException("what the hleyyl ovo se nije smelo dogodit"); //TODO enforce unique in db
        //TODO: prosli gale je smart bio, al validno bi bilo enforcar unique u bazi

        var user = resultList.getFirst();
        if (!authentication(user, password)) throw new IncorrectPasswordException("Password doesn't match");

        log.debug("Login in successful login in as user: {}", user);
        loggedInUser = user;
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static Optional<User> getLoggedInUser() {
        return Optional.ofNullable(loggedInUser);
    }
}
