package io.github.lukagg13.hotelmanagementapp.service;

import io.github.lukagg13.hotelmanagementapp.file.PasswordUtils;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.entity.User;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoginService {
    //get user igs
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private final UsersRepository repository;
    private User loggedInUser;

    public LoginService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }

    public enum AuthorizationLevelOfRole {
        ADMIN(0),
        HOTEL_STAFF(10),
        UNSIGNED(10_000);

        private final Integer authorizationLevel;
        AuthorizationLevelOfRole(Integer authorizationLevel) {
            this.authorizationLevel = authorizationLevel;
        }

        public Integer getAuthorizationLevel() {
            return authorizationLevel;
        }
    }

    private boolean authentication(User user, String password) {
        return PasswordUtils.check(user.getUuid(), password);
    }


    public void login(String name, String password) {
        log.debug("Login attempt in in as: {} with password: {}", name, password);

        //TODO: stavi u db zbog speed il ne igs nije bitno
        var resultList = repository.getAll().stream().filter(user -> user.getUserName().equals(name)).toList();
        if (resultList.isEmpty()) throw  new IllegalStateException("No user found"); //TODO: ILI throw custom exeption mislim da cu trhowat coustom exeptionen tu da se rijseim ovoh uvjeta za prjektni i bolej je bacat expetion
        if (resultList.size() > 1) throw new RuntimeException("what the hleyyl ovo se nije smelo dogodit"); //TODO enforce unique in db
        //TODO: prosli gale je smart bio, al validno bi bilo enforcar unique u bazi

        var user = resultList.getFirst();
        if (!authentication(user, password)) throw new IllegalStateException("Password doesn't match"); //TODO: coustom

        log.debug("Login in successful login in as user: {}", user);
        loggedInUser = user;
    }

    public void logout() {
        loggedInUser = null;
    }

    public Optional<User> getLoggedInUser() {
        return Optional.of(loggedInUser);
    }
}
