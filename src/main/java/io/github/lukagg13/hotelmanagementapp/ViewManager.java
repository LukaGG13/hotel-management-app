package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.exception.DatabaseException;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.View;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class ViewManager {
    private static final Logger log = LoggerFactory.getLogger(ViewManager.class);
    private static Stage stage;
    private static Scene scene;

    //private static final GuestService guestService = new GuestService(new GuestRepository(DatabaseUtils.createConnection()));

    public static GuestService getGuestService() {
        try {
            return new GuestService(new GuestRepository(DatabaseUtils.createConnection()));
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    //TODO: switch to service manager ??
    public static RoomService getRoomService() {
        try {
            return new RoomService(new RoomRepository(DatabaseUtils.createConnection()));
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
    }

    public enum ViewPath {
        LOGIN(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/login-view.fxml")),
        ROOM_CREATE(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/room-create.fxml")),
        ROOM_SEARCH(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/room-search.fxml")),
        BOOKING(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/booking-view.fxml")),
        BOOKING_CREATE(ViewPath.class.getResource("/io/github/lukagg13/hotelmanagementapp/booking-create.fxml")),
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

    public static void switchView(String view, Object controller) {
        if(stage == null) throw new IllegalStateException("No stage. Set the stage first");

        log.debug("url  => {}", ViewManager.class.getResource(view));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewManager.class.getResource(view));
            /*
            switch (view) {
                case "login-view.fxml" -> fxmlLoader.setController(new LoginController(new LoginService(new UsersRepository(DatabaseUtils.createConnection()))));
                //case "hello-view.fxml" -> fxmlLoader.setController(new HelloController());
            }
             */
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

    public static void switchView(ViewPath viewPath, Object controller) {
        switchView(viewPath.path.getPath(), controller);
    }
}
