package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.exception.NotLoggedInException;
import io.github.lukagg13.hotelmanagementapp.repository.BookingRepository;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;
import io.github.lukagg13.hotelmanagementapp.service.BookingService;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.controller.LoginController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.booking.BookingController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.history.HistoryController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class ViewManager {
    private static final Logger log = LoggerFactory.getLogger(ViewManager.class);
    private static Stage stage;
    private static Scene scene;

    public enum ViewPath {
        LOGIN(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/login-view.fxml")),
        ROOM(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/room-view.fxml")),
        ROOM_CREATE(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/room-create.fxml")),
        ROOM_SEARCH(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/room-search.fxml")),
        BOOKING(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/booking-view.fxml")),
        BOOKING_CREATE(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/booking-create.fxml")),
        GUEST(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/guest-view.fxml")),
        GUEST_COMPONENT(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/guest-component.fxml")),
        GUEST_SEARCH(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/guest-search.fxml")),
        GUEST_CREATE(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/guest-create.fxml")),
        HISTORY_VIEW(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/history-view.fxml"));


        public final URL path;
        ViewPath(URL path){
           this.path = path;
        }
    }

    private ViewManager(){}

    public static void setStage(Stage stage) {
        ViewManager.stage = stage;
        scene = new Scene(new Pane());
        stage.setScene(scene);
        stage.setTitle("Hotel management");

        stage.setMaximized(true);
        stage.show();
    }

    public static void switchView(URL view, Object controller) {
        if(stage == null) throw new IllegalStateException("No stage. Set the stage first");

        log.debug("url  => {}", view);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(view);
            if(controller == null) throw new IllegalArgumentException("Controller can't be null.");
            fxmlLoader.setController(controller);

            var root = fxmlLoader.load();
            scene.setRoot((Parent) root);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("View {} doesn't exist", view);
        }
    }

    public static void switchView(ViewPath viewPath) {
        if(LoginService.getLoggedInUser().isEmpty() && !viewPath.equals(ViewPath.LOGIN)) throw new NotLoggedInException("User is not logged in");
        var controller = switch (viewPath) {
            case LOGIN -> new LoginController();
            case GUEST -> new GuestController(new GuestService(new GuestRepository(DatabaseUtils.createConnection())));
            case ROOM ->  new RoomController(new RoomService(new RoomRepository(DatabaseUtils.createConnection())));
            case BOOKING -> new BookingController(new BookingService(new BookingRepository(DatabaseUtils.createConnection())));
            case HISTORY_VIEW -> new HistoryController();
            case null, default -> null;
        };
        switchView(viewPath.path, controller);
    }
}
