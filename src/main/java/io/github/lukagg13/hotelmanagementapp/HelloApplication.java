package io.github.lukagg13.hotelmanagementapp;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseHelper;
import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        var scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hotel management");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


        try {
            DatabaseHelper.createTables();

        } catch (SQLException e) {
            //TODO: database exception logging bolje rjsenje ne znam dal ovo radi mislim da da zbog logova.
        }

    }
}
