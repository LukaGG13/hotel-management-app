package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import io.github.lukagg13.hotelmanagementapp.ui.controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ViewManager {
    private static final Logger log = LoggerFactory.getLogger(ViewManager.class);
    private static Stage stage;
    private static Scene scene;

    private ViewManager(){}
    public static void setStage(Stage stage) {
        ViewManager.stage = stage;
        scene = new Scene(new Pane());
        stage.setScene(scene);
        stage.setTitle("Hotel management");

        stage.setMaximized(true);
        stage.show();
    }

    public static void switchView(String view) {
        if(stage == null) throw new IllegalStateException("No stage. Set the stage first");

        log.debug("url  => {}", ViewManager.class.getResource(view));
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewManager.class.getResource(view));
            switch (view) {
                case "login-view.fxml" -> fxmlLoader.setController(new LoginController(new LoginService(new UsersRepository(DatabaseUtils.createConnection()))));
                //case "hello-view.fxml" -> fxmlLoader.setController(new HelloController());
            }
            var root = fxmlLoader.load();

            scene.setRoot((Parent) root);

        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("View {} doesn't exist", view);
        }
    }
}
