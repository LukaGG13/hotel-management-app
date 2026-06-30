package io.github.lukagg13.hotelmanagementapp.ui.controller.history;

import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.HistoryRecordLogModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HistoryComponentController extends VBox {
    @FXML private Label nameLabel;
    @FXML private Label timeStampLabel;
    @FXML private Label oldObjectLabel;
    @FXML private Label newObjectLabel;

    public HistoryComponentController(HistoryRecordLogModel historyRecordLogModel) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/history-component.fxml"));

        // Da, točno! Pošto ova klasa 'extends VBox', moramo postaviti root na 'this'.
        // To govori FXML-u da je ovaj Java objekt zapravo vanjski kontejner definiran u FXML datoteci (<fx:root>).
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load history component FXML", e);
        }

        nameLabel.textProperty().bind(historyRecordLogModel.nameProperty());
        timeStampLabel.textProperty().bind(historyRecordLogModel.timeStampProperty());
        oldObjectLabel.textProperty().bind(historyRecordLogModel.oldObjectProperty());
        newObjectLabel.textProperty().bind(historyRecordLogModel.newObjectProperty());
    }
}
