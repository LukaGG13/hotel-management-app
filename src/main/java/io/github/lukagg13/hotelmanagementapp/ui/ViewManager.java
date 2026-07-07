package io.github.lukagg13.hotelmanagementapp.ui;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.entity.Admin;
import io.github.lukagg13.hotelmanagementapp.exception.NotLoggedInException;
import io.github.lukagg13.hotelmanagementapp.exception.OnlyAdminShouldChangeRoomsException;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

/**
 * A class to manage view switching in the app.
 */
public class ViewManager {
    private static final Logger log = LoggerFactory.getLogger(ViewManager.class);
    private static Stage stage;
    private static Scene scene;
    private static final Connection connection = DatabaseUtils.createConnection();

    /**
     * A enum of all the valid VIEWS in the app.
     */
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

    /**
     * A private constructor to hide the implicit one.
     */
    private ViewManager(){}

    /**
     * Set the main stage of the app and set the icon.
     * @param stage The main {@link Stage}.
     */
    public static void setStage(Stage stage) {
        ViewManager.stage = stage;
        scene = new Scene(new Pane());
        stage.setScene(scene);
        var image = new Image("goodhotels.png");
        stage.getIcons().add(image);
        stage.setTitle("Hotel management");

        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Method to switch the view which is being shown in the app.
     * @param view The view which to switch to.
     * @param controller The controller for the view.
     */
    public static void switchView(URL view, Object controller) {
        if(stage == null) throw new IllegalStateException("No stage. Set the stage first");

        log.debug("url: {}", view);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(view);
            if(controller == null) throw new IllegalArgumentException("Controller can't be null.");
            fxmlLoader.setController(controller);

            var root = fxmlLoader.load();
            scene.setRoot((Parent) root);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("View {} doesn't exist", view);
        }
    }

    /**
     * Switches the view.
     * @param viewPath The {@link ViewPath} which will be swaped to.
     */
    public static void switchView(ViewPath viewPath) {
        if(LoginService.getLoggedInUser().isEmpty() && !viewPath.equals(ViewPath.LOGIN)) throw new NotLoggedInException("User is not logged in");
        if(viewPath.equals(ViewPath.ROOM) && !(LoginService.getLoggedInUser().orElse(null) instanceof Admin)) throw new OnlyAdminShouldChangeRoomsException("Only admin should change the rooms");

        var controller = switch (viewPath) {
            case LOGIN -> new LoginController();
            case GUEST -> new GuestController(new GuestService(new GuestRepository(connection)));
            case ROOM ->  new RoomController(new RoomService(new RoomRepository(connection)));
            case BOOKING -> new BookingController(new BookingService(new BookingRepository(connection)));
            case HISTORY_VIEW -> new HistoryController();
            case null, default -> null;
        };
        switchView(viewPath.path, controller);
    }
}