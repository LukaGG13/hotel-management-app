package io.github.lukagg13.hotelmanagementapp.ui.controller;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.exception.NotLoggedInException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HeaderController {

    static final Logger log = LoggerFactory.getLogger(HeaderController.class);
    static final String USER_NOT_LOGGED_IN_ERROR_MESSAGE = "You are not logged in, please login in first Thank you!";

    @FXML
    private void handleLogin() {
        log.debug("handle login clicked");
        ViewManager.switchView(ViewManager.ViewPath.LOGIN);
    }

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

    @FXML
    private void handleRooms() {
        log.debug("handle rooms clicked");
        try {
            ViewManager.switchView(ViewManager.ViewPath.ROOM);
        } catch (NotLoggedInException e) {
            log.error("Trying to access room without being logged in  {}", e.getMessage());
            new Alert(Alert.AlertType.ERROR, USER_NOT_LOGGED_IN_ERROR_MESSAGE).showAndWait();
            handleLogin();
        }
    }

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
}
