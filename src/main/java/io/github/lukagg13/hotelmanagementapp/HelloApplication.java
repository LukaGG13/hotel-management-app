package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Class to start the application.
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage)  {
        ViewManager.setStage(stage);
        ViewManager.switchView(ViewManager.ViewPath.LOGIN);
    }
}
