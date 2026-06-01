package io.github.lukagg13.hotelmanagementapp.service;

/*
import org.example.java.components.AuthorizedTab;
import org.example.java.entity.admin.Admin;
import org.example.java.entity.guest.Guest;
import org.example.java.entity.repository.Repository;
import org.example.java.entity.user.User;
import org.example.java.files.PasswordUtils;

 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record LoginService() {
}
/*
public record LoginService(Repository repository) {
    //get user igs
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public enum AuthorizationLevelOfRole {
        ADMIN(0),
        GUEST(10),
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
        return PasswordUtils.check(user.getId(), password);
    }


    public void login(String name, String password) {
        log.debug("Logg in in as: {} with password: {}",name, password);

        //TODO: stavi u db zbog speed il ne igs nije bitno
        var resultList = repository.getUsers().stream().filter(user -> user.getName().equals(name)).toList();
        if (resultList.isEmpty()) throw  new IllegalStateException("No user found"); //TODO: ILI throw custom exeption mislim da cu trhowat coustom exeptionen tu da se rijseim ovoh uvjeta za prjektni i bolej je bacat expetion
        if (resultList.size() > 1) throw new RuntimeException("what the hleyyl ovo se nije smelo dogodit"); //TODO enforce unique in db

        var user = resultList.getFirst();
        if (!authentication(user, password)) throw new IllegalStateException("Passowrd dont match"); //TODO: coustom

        log.debug("Logg in succeful login in as user: {}", user);
        repository.setActiveUser(user);
    }

    public void logout() {
        repository.setActiveUser(null); //TODO: nuh uh vjerovatno ce bum zbog nullable u optional
    }

    //TODO implement this class
    //public boolean authorization(AuthorizedTab tab) {
    public boolean authorization(AuthorizedTab tab) {
        var permissionLevelOfActiveUser = switch (repository.getActiveUser().orElse(null)) {
            case Admin _ -> AuthorizationLevelOfRole.ADMIN.getAuthorizationLevel();
            case Guest _ -> AuthorizationLevelOfRole.GUEST.getAuthorizationLevel();
            case null, default -> AuthorizationLevelOfRole.UNSIGNED.getAuthorizationLevel();
        };
        log.debug("Checking authorization for tab tab name: {}, user permission levle is: {}", tab, permissionLevelOfActiveUser);
        return tab.getAllowedPermissionLevel() >= permissionLevelOfActiveUser;
    }
}
*/