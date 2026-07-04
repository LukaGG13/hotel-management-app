package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.repository.UsersRepository;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage)  {
        ViewManager.setStage(stage);
        ViewManager.switchView(ViewManager.ViewPath.LOGIN);
        try {
            new LoginService(new UsersRepository(DatabaseUtils.createConnection())).login("Luka","1234");
        } catch (Exception _) {

        }
    }
}
