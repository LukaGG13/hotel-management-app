package io.github.lukagg13.hotelmanagementapp;

import javafx.application.Application;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage)  {
        ViewManager.setStage(stage);
        ViewManager.switchView(ViewManager.ViewPath.LOGIN);
    }
}
