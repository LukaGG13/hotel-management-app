package io.github.lukagg13.hotelmanagementapp.ui.controller;

import io.github.lukagg13.hotelmanagementapp.exception.OnlyAdminShouldChangeRoomsException;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import io.github.lukagg13.hotelmanagementapp.exception.NotLoggedInException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller used to manage the header in the app.
 */
public class HeaderController {

    static final Logger log = LoggerFactory.getLogger(HeaderController.class);
    static final String USER_NOT_LOGGED_IN_ERROR_MESSAGE = "You are not logged in, please login in first Thank you!";

    /**
     * Event that handles the login button and switch the view to it.
     */
    @FXML
    private void handleLogin() {
        log.debug("handle login clicked");
        ViewManager.switchView(ViewManager.ViewPath.LOGIN);
    }

    /**
     * Event that handles the guest button and switch the view to it.
     */
    @FXML
    private void handleGuests() {
        log.debug("handle guests clicked");
        try {
            ViewManager.switchView(ViewManager.ViewPath.GUEST);
        } catch (NotLoggedInException e) {
            log.error("Trying to access guests without being logged in {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        }
    }

    /**
     * Event that handles the guest rooms and switch the view to it.
     */
    @FXML
    private void handleRooms() {
        log.debug("handle rooms clicked");
        try {
            ViewManager.switchView(ViewManager.ViewPath.ROOM);
        } catch (NotLoggedInException e) {
            log.error("Trying to access room without being logged in  {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        } catch (OnlyAdminShouldChangeRoomsException e) {
            log.error("Trying to change rooms while not admin {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Please log in as Admin").showAndWait();
        }
    }

    /**
     * Event that handles the bookings rooms and switch the view to it.
     */
    @FXML
    private void handleBookings() {
        log.debug("handle bookings clicked");
        try {
            ViewManager.switchView(ViewManager.ViewPath.BOOKING);

        } catch (NotLoggedInException e) {
            log.error("Trying to access booking without being logged in {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        }
    }

    /**
     * Event that handles the history rooms and switch the view to it.
     */
    @FXML
    private void handleHistory() {
        log.debug("handle History clicked");
        try {
            ViewManager.switchView(ViewManager.ViewPath.HISTORY_VIEW);
        } catch (NotLoggedInException e) {
            log.error("Trying to access history without being logged in {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        }
    }

    @FXML
    private void handleLogout() {
        log.debug("handle logout clicked");
        try {
            LoginService.logout();
            ViewManager.switchView(ViewManager.ViewPath.LOGIN);
        } catch (NotLoggedInException e) {
            log.error("Trying to access logout without being logged in {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        }
    }
}
