package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuestComponentController extends VBox {
    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;

    public GuestComponentController(GuestModel guestModel) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/guest-component.fxml"));
        fxmlLoader.setRoot(this); //TODO: ? zato sto extenda vbox zbog fxml kako je sturkturiran
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load guest component FXML", e);
        }

        nameLabel.textProperty().bind(guestModel.nameProperty());
        ageLabel.textProperty().bind(guestModel.ageProperty().asString());
    }
}
