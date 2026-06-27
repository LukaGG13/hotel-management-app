package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.ui.controller.booking.BookingController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.setStage(stage);
        GuestController guestController = new GuestController(new GuestService(new GuestRepository(DatabaseUtils.createConnection())));
        ViewManager.switchView("guest-view.fxml", guestController);
    }
}
