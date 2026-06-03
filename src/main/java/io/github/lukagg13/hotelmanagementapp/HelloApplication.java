package io.github.lukagg13.hotelmanagementapp;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewManager.setStage(stage);
        ViewManager.switchView("login-view.fxml");
    }
}
