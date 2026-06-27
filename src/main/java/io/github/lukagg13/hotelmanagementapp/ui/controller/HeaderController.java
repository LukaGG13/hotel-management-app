package io.github.lukagg13.hotelmanagementapp.ui.controller;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.repository.BookingRepository;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.service.BookingService;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.controller.booking.BookingController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomController;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeaderController {

    final static Logger log = LoggerFactory.getLogger(HeaderController.class);
    @FXML
    private void handleHome() throws IOException {
        log.debug("handle home clicked");
        ViewManager.switchView("login-view.fxml", new LoginController(new LoginService(new UsersRepository(DatabaseUtils.createConnection()))));
    }

    @FXML
    private void handleDashboard() throws IOException {
        log.debug("handle dashboard clicked");
        ViewManager.switchView("guest-view.fxml", new GuestController(new GuestService(new GuestRepository(DatabaseUtils.createConnection()))));
    }

    @FXML
    private void handleRooms() throws IOException {
        log.debug("handle rooms clicked");
        ViewManager.switchView("room-view.fxml", new RoomController(new RoomService(new RoomRepository(DatabaseUtils.createConnection()))));
    }

    @FXML
    private void handleBookings() throws IOException {
        log.debug("handle bookings clicked");
        ViewManager.switchView("booking-view.fxml", new BookingController(new BookingService(new BookingRepository(DatabaseUtils.createConnection()))));
    }

    @FXML
    private void handleHistory() throws IOException {
        log.debug("handle History clicked");
        ViewManager.switchView("history-view.fxml", new HistoryController());
    }
}
